package data.structures;

/**
 * A dynamic list implementation used to store elements.
 * 
 * @param <T> the type of elements stored in the list
 */
public class RoadList<T> {

	private Object[] listData = new Object[5000];
	private int size = 0;

	/**
	 * Adds an element to the list.
	 * 
	 * @param value the element to be added
	 */
	public void add(T value) {
		listData[size++] = value;
	}

	public void removeAt(int index) {

		for (int j = index; j < size - 1; j++) {
			listData[j] = listData[j + 1];
		}

		listData[size - 1] = null;
		size--;
	}

	/**
	 * @param value - what we want to remove
	 */
	public void remove(T value) {

		for (int i = 0; i < size; i++) {

			if (listData[i] == value) {

				for (int j = i; j < size - 1; j++) {
					listData[j] = listData[j + 1];
				}

				listData[size - 1] = null;
				size--;
				return;
			}
		}
	}

	/**
	 * Retrieves an element at a specific index.
	 * 
	 * @param ind the index of the element
	 * 
	 * @return the element at the specified index
	 */
	@SuppressWarnings("unchecked")
	public T get(int ind) {
		return (T) listData[ind];
	}

	public int size() {
		return size;
	}
}