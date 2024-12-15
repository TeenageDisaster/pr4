import java.io.*;
import java.net.*;
import java.util.Scanner;

class UDPChatApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Вітаємо в чаті на основі UDP-сокетів!");
        System.out.println("Оберіть режим роботи:");
        System.out.println("1. Запустити сервер");
        System.out.println("2. Запустити клієнт");
        System.out.print("Ваш вибір (1 або 2): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        switch (choice) {
            case 1:
                startUDPServer();
                break;
            case 2:
                startUDPClient();
                break;
            default:
                System.out.println("Невірний вибір. Завершення роботи.");
        }
    }

    public static void startUDPServer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть порт для сервера: ");
        int port = scanner.nextInt();

        try (DatagramSocket serverSocket = new DatagramSocket(port)) {
            System.out.println("UDP-сервер запущено. Очікування повідомлень...");
            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket);

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Отримано від клієнта: " + receivedMessage);

                // Відправлення підтвердження клієнту
                String response = "Сервер отримав: " + receivedMessage;
                byte[] sendBuffer = response.getBytes();
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);
            }
        } catch (IOException e) {
            System.err.println("Помилка запуску сервера: " + e.getMessage());
        }
    }

    public static void startUDPClient() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть IP адресу сервера: ");
        String serverAddress = scanner.nextLine();
        System.out.print("Введіть порт сервера: ");
        int port = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            byte[] sendBuffer;
            byte[] receiveBuffer = new byte[1024];

            System.out.println("Підключено до UDP-сервера: " + serverAddress + ":" + port);

            while (true) {
                System.out.print("Введіть повідомлення (або 'вихід' для завершення): ");
                String message = scanner.nextLine();

                if ("вихід".equalsIgnoreCase(message)) {
                    System.out.println("Завершення роботи клієнта...");
                    break;
                }

                // Відправлення повідомлення серверу
                sendBuffer = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverInetAddress, port);
                clientSocket.send(sendPacket);

                // Отримання відповіді від сервера
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(receivePacket);

                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Відповідь від сервера: " + response);
            }
        } catch (IOException e) {
            System.err.println("Помилка клієнта: " + e.getMessage());
        }
    }
}
