// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: glumacla
// UT Student #: 1006068461
// Author: Lazar Glumac
//
// Student2:
// UTORID user_name: radwanya
// UT Student #: 1006280748
// Author: Yara Radwan
//
// Student3:
// UTORID user_name: blasett1
// UT Student #: 1005991198
// Author: Ryan Blasetti
//
// Student4:
// UTORID user_name: laiking2
// UT Student #: 1006030723
// Author: King Lai
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************

package filesystem;

/**
 * Represents an ItemList.
 *
 * @param <E>
 * 
 * @version 1.0
 */
public class ItemList<E> {

  // An ArrayList holding elements of type E
  private java.util.ArrayList<E> itemList;

  /**
   * Creates an instance of an ItemList Object. Initializes itemList to an empty ArrayList.
   */
  public ItemList() {
    this.itemList = new java.util.ArrayList<E>();
  }

  /**
   * Returns the length of itemList.
   * 
   * @return the length of itemList
   */
  public int size() {
    return this.itemList.size();
  }

  /**
   * Adds an element to the end of itemList.
   * 
   * @param element the element to be added
   */
  public void add(E element) {
    this.itemList.add(element);
  }

  /**
   * Removes an element from itemList given the index.
   * 
   * @param index the index of the element to be removed
   */
  public void remove(int index) {
    this.itemList.remove(index);
  }

  /**
   * Returns an element at a given index in itemList.
   * 
   * @param index the index of the element in itemList
   * @return the element in itemList with the specified index
   */
  public E get(int index) {
    return this.itemList.get(index);
  }
  
  /*
   * IMPORTANT:
   * The methods in this class all call methods from ArrayList, and thus it was necessary to do
   * JUnit testing for ItemList.
   */
}
