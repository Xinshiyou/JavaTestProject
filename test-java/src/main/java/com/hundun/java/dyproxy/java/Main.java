package com.hundun.java.dyproxy.java;

public class Main {

	public static void main(String[] args) {

		BookFacadeImpl bookFacadeImpl = new BookFacadeImpl();
		BookFacadeProxy proxy = new BookFacadeProxy();
		BookFacade bookfacade = (BookFacade) proxy.bind(bookFacadeImpl);
		bookfacade.addBook();

	}

}
