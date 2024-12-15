import java.io.*;
import java.net.*;
import java.util.Scanner;

 class TCPChatApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Вітаємо в чаті на основі TCP-сокетів!");
        System.out.println("Оберіть режим роботи:");
        System.out.println("1. Запустити сервер");
        System.out.println("2. Запустити клієнт");
        System.out.print("Ваш вибір (1 або 2): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        switch (choice) {
            case 1:
                startServer();
                break;
            case 2:
                startClient();
                break;
            default:
                System.out.println("Невірний вибір. Завершення роботи.");
        }
    }

    public static void startServer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть порт для сервера: ");
        int port = scanner.nextInt();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущено. Очікування підключень...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Підключено клієнта: " + clientSocket.getInetAddress());

                // Обробка клієнтського з'єднання
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Помилка запуску сервера: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Клієнт: " + message);
                out.println("Сервер: " + message); // Ехо-відповідь
            }
        } catch (IOException e) {
            System.err.println("Помилка у з'єднанні з клієнтом: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Помилка закриття клієнтського сокета: " + e.getMessage());
            }
        }
    }

    public static void startClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть IP адресу сервера: ");
        String serverAddress = scanner.nextLine();
        System.out.print("Введіть порт сервера: ");
        int port = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Підключено до сервера: " + serverAddress + ":" + port);
            String message;

            while (true) {
                System.out.print("Введіть повідомлення (або 'вихід' для завершення): ");
                message = console.readLine();

                if ("вихід".equalsIgnoreCase(message)) {
                    System.out.println("Завершення з'єднання...");
                    break;
                }

                // Відправка повідомлення серверу
                out.println(message);

                // Отримання відповіді від сервера
                String response = in.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Помилка клієнта: " + e.getMessage());
        }
    }
}
