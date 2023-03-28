import java.io.*;
import java.util.Arrays;

public class Basket implements Serializable {
    private static final long serialVersionUID = 1973L;
    private String[] productsBasket;
    private int[] pricesBasket;
    private int[] amountsBasket;

    public Basket(String[] productsBasket, int[] pricesBasket) {
        this.productsBasket = productsBasket;
        this.pricesBasket = pricesBasket;
        this.amountsBasket = new int[productsBasket.length];
    }

    public Basket(String[] productsBasket, int[] pricesBasket, int[] amountsBasket) {
        this.productsBasket = productsBasket;
        this.pricesBasket = pricesBasket;
        this.amountsBasket = amountsBasket;
    }

    public void addToCart(int productNum, int amount) {
        amountsBasket[productNum] += amount;
    }

    public void printCart() {
        int summaryBasket = 0;
        for (int i = 0; i < amountsBasket.length; i++) {
            if (!(amountsBasket[i] == 0)) {
                int currentPrice = (amountsBasket[i] * pricesBasket[i]);
                summaryBasket += currentPrice;
                System.out.printf("%s %d шт. по %d руб./шт. - %d руб в сумме; \n",
                        productsBasket[i], amountsBasket[i], pricesBasket[i], currentPrice);
            }
        }
        System.out.printf("Итого: %d руб. \n", summaryBasket);
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (var product : productsBasket)
                out.print(product + " ");
            out.println();
            for (var price : pricesBasket)
                out.print(price + " ");
            out.println();
            for (var amount : amountsBasket)
                out.print(amount + " ");
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            // первая строка файла
            String[] productsLoad = (br.readLine()).split(" ");
            // вторая строка файла
            int[] pricesLoad = Arrays.stream((br.readLine()).split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
            // третья строка файла
            int[] amountsLoad = Arrays.stream((br.readLine()).split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
            return new Basket(productsLoad, pricesLoad, amountsLoad);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Basket:" +
                "\nproductsBasket=" + Arrays.toString(productsBasket) +
                "\npricesBasket=" + Arrays.toString(pricesBasket) +
                "\namounts=" + Arrays.toString(amountsBasket);
    }
}
