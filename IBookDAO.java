package dal;

import java.util.List;

import dto.BookDTO;

public interface IBookDAO {
	public void createBook(BookDTO book);

	public BookDTO getBook(String title);

	public List<BookDTO> getAllBooks();

	public void updateBook(String cr, BookDTO book);

	public void deleteBook(String title);
	
	public int getBookID(BookDTO book);
}
