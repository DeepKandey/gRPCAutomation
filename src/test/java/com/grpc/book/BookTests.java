package com.grpc.book;

import client.BookClient;
import com.endpoint.examples.bookstore.BookResponse;
import exception.AuthorNotFoundException;
import exception.BookNotFoundException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookTests extends BaseTest {
  private BookClient bookClient;

  public BookTests() {
    bookClient = new BookClient();
  }

  @Test
  public void getBookTest() throws BookNotFoundException {
    BookResponse bookResponse = bookClient.getBookByISBN(1);
    Assert.assertEquals(bookResponse.getResponseCode(), "200");
  }

  @Test
  public void getBookViaAuthorTest() throws AuthorNotFoundException {
    BookResponse bookResponse = bookClient.getBookByAuthor("Bob");
    Assert.assertEquals(bookResponse.getResponseCode(), "200");
  }
}
