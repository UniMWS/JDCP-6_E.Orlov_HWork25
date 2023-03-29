import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static File logFile = new File("log.csv");
    public static File jsonFile = new File("basket.json");
    public static File jsonFileTmp = new File("basketTmp.json");// кривая попытка
    public static String[] products = {"Молоко", "Хлеб", "Гречневая крупа"};
    public static int[] prices = {50, 14, 80};

    public static void main(String[] args) throws IOException {
        System.out.println("JDCP-6 + Евгений Орлов + ДЗ-23 + " +
                "Потоки ввода-вывода. Работа с файлами. Сериализация");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Задача 1\n");
        Basket basket = new Basket();
        ClientLog clientLog = new ClientLog();

        // не знаю как JSON в Main, так что только GSON в Basket
        JSONObject basketJson = new JSONObject();
        JSONParser basketParser = new JSONParser();
        // не работает, потому что saveBasketJSON кривой
//        parserBasketJSON(basketParser, jsonFileTmp);

        if (jsonFile.exists()) {// проверка существования файла
            System.out.println("Корзина уже существует и будет использована:");
            basket = Basket.loadFromJSON(jsonFile);//загрузка корзины из файла
            basket.printCart();
        } else {
            System.out.print("Корзина пуста. ");
            basket = new Basket(products, prices);
        }
        groceryList(basket);// список продуктов
        while (true) {
            System.out.println("\nВыберите товар и количество через пробел " +
                    "или введите \"end\" для выхода:");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println(String.format("Надо вводить два числа \"Номер Количество\"" +
                        " через пробел, а не '%s'", input));
                continue;
            }
            try {
                if (Integer.parseInt(parts[0]) < 0 || Integer.parseInt(parts[0]) > products.length) {
                    System.out.println(String.format("Надо вводить номер товара от '1' до '%s'",
                            (products.length)));
                } else if (Integer.parseInt(parts[1]) >= 0) {
                    int productNumber = Integer.parseInt(parts[0]) - 1;//номер продукта
                    int productCount = Integer.parseInt(parts[1]);//штук продукта
                    basket.addToCart(productNumber, productCount);// ушло в корзину
                    basket.saveToJSON(jsonFile);
                    clientLog.log(productNumber + 1, productCount);
                } else
                    System.out.println(String.format("Количество товара не может быть отрицательным" +
                            " '%s'", Integer.parseInt(parts[1])));
            } catch (NumberFormatException e) {
                System.out.println(String.format("Ошибка ввода, вы ввели не два числа через пробел:" +
                        " '%s'", input));
            }
        }
        System.out.println("Ваша корзина:");
        basket.printCart();// печать корзины
        clientLog.exportAsCSV(logFile);
        // кривой "basketTmp.json"
        saveBasketJSON(basket, basketJson, jsonFileTmp);
    }

    // не знаю как JSON, так что только GSON
    private static void saveBasketJSON(Basket basket, JSONObject basketJson, File textFile) {
        basketJson.put("productsBasket", basket.getProductsBasket());
        basketJson.put("pricesBasket", basket.getPricesBasket());
        basketJson.put("amountsBasket", basket.getAmountsBasket());
        try (FileWriter fileWriter = new FileWriter(textFile)) {
            fileWriter.write(basketJson.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // не знаю как JSON, так что только GSON
    private static JSONObject parserBasketJSON(JSONParser basketParser, File textFile)
            throws IOException {
        try {
            Object obj = basketParser.parse(new FileReader(textFile));
            return (JSONObject) obj;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // список продуктов
    private static void groceryList(Basket basket) throws IOException {
        System.out.println("Список доступных для покупки продуктов:");
        for (int i = 0; i < products.length; i++) {
            System.out.printf("%d. %s %d руб/шт \n", i + 1, products[i], prices[i]);
        }
    }
}