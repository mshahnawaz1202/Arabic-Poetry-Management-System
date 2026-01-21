package bl;

import java.util.List;

import dto.BookDTO;

public interface IBookBO {
	void createBook(BookDTO book);

	BookDTO getBook(String title);

	List<BookDTO> getAllBooks();

	void updateBook(String currentTitle, BookDTO book);

	void deleteBook(String title);

	int getBookID(BookDTO book);
}