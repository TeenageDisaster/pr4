import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;

class HTTPClientApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Вітаємо у консольному HTTP-клієнті!");
        System.out.println("Оберіть тип запиту:");
        System.out.println("1. GET-запит");
        System.out.println("2. POST-запит");
        System.out.print("Ваш вибір (1 або 2): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера

        switch (choice) {
            case 1:
                performGETRequest();
                break;
            case 2:
                performPOSTRequest();
                break;
            default:
                System.out.println("Невірний вибір. Завершення роботи.");
        }
    }

    public static void performGETRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть URL для GET-запиту: ");
        String url = scanner.nextLine();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            System.out.println("Статус-код: " + response.statusCode());
            System.out.println("Заголовки:");
            response.headers().map().forEach((k, v) -> System.out.println(k + ": " + v));
            System.out.println("Тіло відповіді:");
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("Помилка виконання GET-запиту: " + e.getMessage());
        }
    }

    public static void performPOSTRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть URL для POST-запиту: ");
        String url = scanner.nextLine();
        System.out.print("Введіть дані для надсилання (у форматі JSON): ");
        String jsonData = scanner.nextLine();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            System.out.println("Статус-код: " + response.statusCode());
            System.out.println("Заголовки:");
            response.headers().map().forEach((k, v) -> System.out.println(k + ": " + v));
            System.out.println("Тіло відповіді:");
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            System.err.println("Помилка виконання POST-запиту: " + e.getMessage());
        }
    }
}
