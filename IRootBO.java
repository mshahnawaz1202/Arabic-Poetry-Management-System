package bl.services;

import java.util.List;

import dto.RootDTO;

public interface IRootBO {

	List<RootDTO> extractRootsAllVerses();

	int extractAndSaveAllRoots();

	List<RootDTO> getAllRootsFromDatabase();

	List<RootDTO> getRootsByVerseId(int verseId);

	void deleteAllRoots();

	int getRootCount();

}
