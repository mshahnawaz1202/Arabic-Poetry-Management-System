package bl;

import java.util.List;

import dal.IDALFacade;
import dto.BookDTO;

public class BookBO implements IBookBO {
	private IDALFacade dalfacade;

	public BookBO(IDALFacade dalfacade) {
		this.dalfacade = dalfacade;
	}

	@Override
	public void createBook(BookDTO book) {
		if (book == null) {
			throw new IllegalArgumentException("Book cannot be null");
		}
		if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("Book title cannot be null or empty");
		}
		if (book.getTitle().length() > 100) {
			throw new IllegalArgumentException("Book title cannot exceed 100 characters");
		}
		if (book.getCompiler() != null && book.getCompiler().length() > 100) {
			throw new IllegalArgumentException("Compiler name cannot exceed 100 characters");
		}
		int existingBook = dalfacade.getBookID(book);
		if (existingBook != 0) {
			throw new IllegalStateException("Book with title '" + book.getTitle() + "' already exists");
		}
		dalfacade.createBook(book);
	}

	@Override
	public BookDTO getBook(String title) {
		BookDTO book = dalfacade.getBook(title);
		if (book == null) {
			throw new IllegalArgumentException("Book with title " + title + " does not exist");
		}
		return book;
	}

	@Override
	public List<BookDTO> getAllBooks() {
		List<BookDTO> books = dalfacade.getAllBooks();
		if (books == null || books.isEmpty()) {
			throw new IllegalStateException("No books found in the database");
		}

		return books;
	}

	@Override
	public void updateBook(String currentTitle, BookDTO book) {
		if (currentTitle == null || currentTitle.trim().isEmpty()) {
			throw new IllegalArgumentException("Current title cannot be null or empty");
		}

		if (book == null) {
			throw new IllegalArgumentException("Book details cannot be null");
		}

		String newTitle = book.getTitle();
		if (newTitle == null || newTitle.trim().isEmpty()) {
			throw new IllegalArgumentException("New title cannot be null or empty");
		}

		if (newTitle.length() > 100) {
			throw new IllegalArgumentException("New title cannot exceed 100 characters");
		}

		// Verify book exists
		BookDTO existingBook = dalfacade.getBook(currentTitle);
		if (existingBook == null) {
			throw new IllegalArgumentException("Book with title '" + currentTitle + "' does not exist");
		}

		// Check for duplicates only if title is changing
		if (!currentTitle.equals(newTitle)) {
			BookDTO duplicateCheck = dalfacade.getBook(newTitle);
			if (duplicateCheck != null) {
				throw new IllegalStateException("Book with title '" + newTitle + "' already exists");
			}
		}

		dalfacade.updateBook(currentTitle, book);
	}

	@Override
	public void deleteBook(String title) {
		dalfacade.deleteBook(title);
	}

	@Override
	public int getBookID(BookDTO book) {
		return dalfacade.getBookID(book);
	}
}