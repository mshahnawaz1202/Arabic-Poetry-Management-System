package bl;

import dal.IDALFacade;
import dto.BookDTO;
import dto.ImportResultDTO;
import dto.PoemDTO;

import java.io.*;
import java.sql.Date;
import java.util.*;
import java.util.regex.*;

/***
 * @author shahn
 */

public class ImportBookBO implements IImportBookBO {
    private IDALFacade dal;

    public ImportBookBO(IDALFacade dal) {
        this.dal = dal;
    }

    @Override
    public ImportResultDTO importBook(File file) {
        // Validate input parameters
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }

        ImportResultDTO result = new ImportResultDTO();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append("\n");
        } catch (IOException e) {
            result.setMessage("❌ Error reading file: " + e.getMessage());
            return result;
        }

        String text = sb.toString();
        String bookTitle = extractBetween(text, "الكتاب :", "\n").trim();
        if (bookTitle.isEmpty())
            bookTitle = "Untitled";

        BookDTO book = new BookDTO();
        book.setTitle(bookTitle);
        book.setCompiler("Unknown");
        book.setEra(new Date(System.currentTimeMillis()));

        try {
            dal.createBook(book);
        } catch (Exception e) {
            result.setMessage("❌ Error saving book: " + e.getMessage());
            return result;
        }

        Pattern poemPattern = Pattern.compile("\\[(.*?)\\]");
        Matcher poemMatcher = poemPattern.matcher(text);

        int poemsCount = 0;
        while (poemMatcher.find()) {
            String poemTitle = poemMatcher.group(1).trim();

            int start = poemMatcher.end();
            int next = text.indexOf("[", start);
            String chunk = (next == -1) ? text.substring(start) : text.substring(start, next);

            Pattern versePattern = Pattern.compile("\\((.*?)\\)");
            Matcher vm = versePattern.matcher(chunk);
            List<String> verses = new ArrayList<>();
            while (vm.find())
                verses.add(vm.group(1).trim());

            if (verses.isEmpty())
                continue;

            PoemDTO poem = new PoemDTO();
            poem.setTitle(poemTitle);
            poem.setBookId(0);
            poem.setPoetId(0);

            try {
                dal.savePoem(poem, verses, book);
                poemsCount++;
            } catch (Exception e) {
                System.err.println("⚠ Error saving poem: " + poemTitle + " -> " + e.getMessage());
            }
        }

        result.setBookTitle(bookTitle);
        result.setPoemsImported(poemsCount);
        result.setMessage("✅ Imported " + poemsCount + " poems successfully.");
        return result;
    }

    private String extractBetween(String text, String start, String end) {
        int s = text.indexOf(start);
        if (s == -1)
            return "";
        s += start.length();
        int e = text.indexOf(end, s);
        if (e == -1)
            e = text.length();
        return text.substring(s, e);
    }
}
