package models;

import java.util.ArrayList;
import java.util.HashSet;

public class HistoryBundle {
	private ArrayList<History> historyList;
	private HashSet<String> historySet;
	private int deleteAfter;

	public HistoryBundle(ArrayList<History> historyList, HashSet<String> historySet, int deleteAfter) {
		this.historyList = historyList;
		this.historySet = historySet;
		this.deleteAfter = deleteAfter;
	}

	public ArrayList<History> getHistoryList() {
		return historyList;
	}

	public int getDeleteAfter() {
		return deleteAfter;
	}

	public HashSet<String> getHistorySet() {
		return historySet;
	}

	public void add(History history) {
		historyList.add(history);
		historySet.add(history.getId());
	}

	public boolean historyContainsID(String id) {
		return historySet.contains(id);
	}

}
