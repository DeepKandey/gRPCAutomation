package client;

import com.endpoint.examples.bookstore.BookAuthorRequest;
import com.endpoint.examples.bookstore.BookResponse;
import com.endpoint.examples.bookstore.BookServiceGrpc;
import com.endpoint.examples.bookstore.GetBookRequest;
import exception.AuthorNotFoundException;
import exception.BookNotFoundException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BookClient {
  ManagedChannel channel;

  BookServiceGrpc.BookServiceBlockingStub bookServiceStub;

  public BookClient() {
    channel = ManagedChannelBuilder.forAddress("localhost", 50055).usePlaintext().build();
    bookServiceStub = BookServiceGrpc.newBlockingStub(channel);
  }

  public BookResponse getBookByISBN(Integer ISBN) throws BookNotFoundException {
    try {
      GetBookRequest getBookRequest = GetBookRequest.newBuilder().setIsbn(ISBN).build();
      return bookServiceStub.getBook(getBookRequest);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new BookNotFoundException("Book not found");
    }
  }

  public BookResponse getBookByAuthor(String authorName) throws AuthorNotFoundException {
    try {
      BookAuthorRequest getBookRequest =
          BookAuthorRequest.newBuilder().setAuthor(authorName).build();
      return bookServiceStub.getBookViaAuthor(getBookRequest);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new AuthorNotFoundException("Author details not found");
    }
  }
}
