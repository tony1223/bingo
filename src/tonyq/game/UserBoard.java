package tonyq.game;

public class UserBoard {
	private int nums[] = null; // [¸¹½X] = ¦ì¸m
	private static int MAX_SIZE = 25;
	int index = 0;

	boolean cancel = false;

	public UserBoard() {
		nums = new int[MAX_SIZE];
	}

	public boolean isCancel() {
		return cancel && index == 0;
	}

	public boolean isFull() {
		return index == MAX_SIZE;
	}

	public int getIndex() {
		return index;
	}

	public int getNumber(int location){
		return nums[location];
	}
	public int getWaitingCount() {
		return (MAX_SIZE - index) + 1;
	}

	public boolean isCancelable() {
		return index > 0;
	}

	public boolean isSummitable() {
		return isFull();
	}

	public boolean isRandomable() {
		return index == 0;
	}

	public boolean isLocationSelected(int location) {
		// for (int i = 0; i < nums.length; i++) {
		// if (nums[i] == num) {
		// return true;
		// }
		// }
		// return false;
		return nums[location] == 0;
	}

	public void doSelect(int location) {

		if (isFull()) {
			throw new RuntimeException("user board is full");
		}
		cancel = false;
		nums[location] = index;
		++index;
	}

	public void cancel() {
		cancel = true;
		nums = new int[25];
		index = 0;
	}

	public void random() {
		cancel = false;
		nums = getRandomNumArray();
		index = MAX_SIZE;
	}

	private int[] getRandomNumArray() {

		int[] check = new int[MAX_SIZE];
		int[] com = new int[MAX_SIZE];

		for (int i = 0; i < MAX_SIZE; i++) {

			int number = (int) (Math.random() * MAX_SIZE) + 1;

			while (check[number - 1] != 0) {
				number = (int) (Math.random() * MAX_SIZE) + 1;
			}

			com[i] = number;
			check[number - 1] = 1;

		}

		return com;

	}
}
