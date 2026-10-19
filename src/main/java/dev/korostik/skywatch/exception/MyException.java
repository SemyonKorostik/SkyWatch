package dev.korostik.skywatch.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyException extends Exception {

  private String timestamp;
  private int status;
  private String error;
  private String message;
  private String path;

}
