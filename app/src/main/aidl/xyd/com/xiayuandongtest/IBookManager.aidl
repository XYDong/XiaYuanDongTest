// IBookManager.aidl
package xyd.com.xiayuandongtest;

// Declare any non-default types here with import statements
import xyd.com.xiayuandongtest.entity.Book;
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            List<Book> getListBook();
            void addBook(in Book book);


}
