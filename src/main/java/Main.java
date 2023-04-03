import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static String[] products = {"Молоко", "Хлеб", "Гречневая крупа"};
    public static int[] prices = {50, 14, 80};

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        System.out.println("JDCP-6 + Евгений Орлов + ДЗ-25 + " +
                "Работа с файлами CSV, XML, JSON");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Задача 2\n");

        SetFromXML setFromXML = new SetFromXML(new File("shop.xml"));
        File loadFile = new File(setFromXML.loadFile);
        File saveFile = new File(setFromXML.saveFile);
        File logFile = new File(setFromXML.logFile);

        Basket basket = selectBasket(loadFile, setFromXML.isLoad, setFromXML.loadFormat);
        ClientLog clientLog = new ClientLog();

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
                    if (setFromXML.isSave) {
                        switch (setFromXML.saveFormat) {
                            case "json" -> basket.saveToJSON(saveFile);
                            case "txt" -> basket.saveTxt(saveFile);
                        }
                    }
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
        if (setFromXML.isLog) clientLog.exportAsCSV(logFile);
    }

    private static Basket selectBasket(File loadFile, boolean isLoad, String loadFormat)
            throws FileNotFoundException {
        Basket basket;
        if (isLoad && loadFile.exists()) {
            System.out.println("Корзина уже существует и будет использована:");
            basket = switch (loadFormat) {
                case "json" -> Basket.loadFromJSON(loadFile);
                case "txt" -> Basket.loadFromTxtFile(loadFile);
                default -> new Basket(products, prices);
            };
            basket.printCart();
        } else {
            basket = new Basket(products, prices);
            System.out.print("Корзина не существует или пуста. ");
        }
        return basket;
    }

    private static void groceryList(Basket basket) throws IOException {
        System.out.println("Список доступных для покупки продуктов:");
        for (int i = 0; i < products.length; i++) {
            System.out.printf("%d. %s %d руб/шт \n", i + 1, products[i], prices[i]);
        }
    }
}