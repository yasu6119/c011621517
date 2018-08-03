
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 幡野
 */
public class Tresure {

    public static Random r = new Random();
    static Scanner input = new Scanner(System.in);

    public static void mainTresure() {

        System.out.println("宝箱を見つけた！");
        input.nextLine();
        System.out.println("開けますか？\n"
                + "0→開ける\n"
                + "1→立ち去る");
        while (true) {
            String str = input.next();
            if (str.equals("0")) {

                int a = r.nextInt(4);
                if (a <= 21) {
                    getCard();
                    break;
                } else {
                    getItem();
                    break;
                }
            } else if (str.equals("1")) {
                break;
            }
        }

    }

    public static void getItem() {
        System.out.print("宝箱の中身は");
        int a = r.nextInt(3);
        if (a == 0) {
            System.out.println("HPポーションだった");
            Player.getItems()[0]++;
            input.nextLine();

            System.out.println("HPポーションを入手しました");
        } else if (a == 1) {
            System.out.println("MPポーションだった");
            Player.getItems()[1]++;
            input.nextLine();
            System.out.println("MPポーションを入手しました");
        } else {
            System.out.println("リカバーハーブだった");
            Player.getItems()[2]++;
            input.nextLine();
            System.out.println("リカバーハーブを入手しました");
        }

    }

    public static void getCard() {
        List<Integer> cardList = new ArrayList<Integer>();
        Card c;
        for (int i = 0; i < 3; i++) {
            int a = r.nextInt(Player.RandomCardList.size());
            cardList.add(Player.RandomCardList.get(a));

        }
        int b = 0;
        System.out.println("宝箱の中身はカードだった\n"
                + "獲得するカードを選んでください");
        for (Integer integer : cardList) {
            c = new Card(integer);
            System.out.println(b + "," + c.Text());
            b++;
        }
        while (true) {
            String str = input.next();
            try {
                Player.getDeck().addCard(cardList.get(Integer.parseInt(str)));
                System.out.println("カードを入手した");
                input.nextLine();
                break;
            } catch (Exception e) {
            }
        }

    }
}
