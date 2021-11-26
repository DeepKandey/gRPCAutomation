package server;

import com.endpoint.examples.bookstore.BookAuthorRequest;
import com.endpoint.examples.bookstore.BookResponse;
import com.endpoint.examples.bookstore.BookServiceGrpc;
import com.endpoint.examples.bookstore.GetBookRequest;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class BookStoreServer {
  private static final Logger logger = Logger.getLogger(BookStoreServer.class.getName());

  private Server server;

  /** Main launches the server from the command line. */
  public static void main(String[] args) throws Exception {
    final BookStoreServer server = new BookStoreServer();
    server.start();
    server.blockUntilShutdown();
  }

  public void start() throws IOException {
    /*
    The port on which the server should run
     */
    int port = 50055;
    server = ServerBuilder.forPort(port).addService(new BookStoreServiceImpl()).build().start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                  System.err.println("****** shutting down gRPC server since JVM is shutting down");
                  try {
                    BookStoreServer.this.stop();
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                }));
  }

  public void stop() throws InterruptedException {
    if (server != null) {
      server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }
}

class BookStoreServiceImpl extends BookServiceGrpc.BookServiceImplBase {

  @Override
  public void getBook(GetBookRequest request, StreamObserver<BookResponse> responseObserver) {
    BookResponse.Builder response = BookResponse.newBuilder();

    int isbn = request.getIsbn();

    if (isbn == 1) {
      response.setResponseCode("200").setMessage("Success").build();
    } else {
      response.setResponseCode("200").setMessage("Failed").build();
    }

    responseObserver.onNext(response.build());

    responseObserver.onCompleted();
  }

  @Override
  public void getBookViaAuthor(
      BookAuthorRequest request, StreamObserver<BookResponse> responseObserver) {
    BookResponse.Builder response = BookResponse.newBuilder();

    String author = request.getAuthor();

    System.out.println("Author Request: " + author);

    if (author.equals("Bob")) {
      response.setResponseCode("200").setMessage("Success").build();
    } else {
      response.setResponseCode("200").setMessage("Failed").build();
    }

    responseObserver.onNext(response.build());

    responseObserver.onCompleted();
  }
}
