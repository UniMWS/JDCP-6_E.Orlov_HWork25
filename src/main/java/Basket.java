import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Arrays;

public class Basket {
    private String[] productsBasket;
    private int[] pricesBasket;
    private int[] amountsBasket;

    public Basket() {
    }

    public Basket(String[] productsBasket, int[] pricesBasket) {
        this.productsBasket = productsBasket;
        this.pricesBasket = pricesBasket;
        this.amountsBasket = new int[productsBasket.length];
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

    // переделать бы в JSON как-то - не в этой жизни
    public void saveToJSON(File textFile) throws IOException {
        FileWriter fileWriter = new FileWriter(textFile);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        fileWriter.write(gson.toJson(this, Basket.class));
        fileWriter.close();
    }

    public static Basket loadFromJSON(File textFile) throws FileNotFoundException {
        Gson gson = new Gson();
        FileReader fileReader = new FileReader(textFile);
        return gson.fromJson(fileReader, Basket.class);
    }

    public String[] getProductsBasket() {
        return productsBasket;
    }

    public int[] getPricesBasket() {
        return pricesBasket;
    }

    public int[] getAmountsBasket() {
        return amountsBasket;
    }

    @Override
    public String toString() {
        return "Basket:" +
                "\nproductsBasket=" + Arrays.toString(productsBasket) +
                "\npricesBasket=" + Arrays.toString(pricesBasket) +
                "\namountsBasket=" + Arrays.toString(amountsBasket);
    }
}
