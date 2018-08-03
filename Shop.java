
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
 * @author 大河内
 */
public class Shop {

    public static Random r = new Random();
    static Scanner input = new Scanner(System.in);

    public static void mainShop() {
        System.out.println("<<<   ＳＨＯＰ   >>>");
        System.out.println("店主「いらっしゃい！どれも 20G だよ！」");

        int a = 0;
        while (true) {
            System.out.println("c→カードを購入"
                    + ",i→アイテムを購入"
                    + ",r→デッキのカードを削除"
                    + ",e→ショップを出る\n"
                    + ""
                    + "どうする...?(所持金 " + Player.getMoney() + "G)");
            String n = input.next();
            if (n.equals("e")) {
                a = 1;
                System.out.println("店主「毎度あり！」");
            } else if (Player.getMoney() < 20) {
                System.out.println("店主「お金が足りないよ！」");
            } else {
                switch (n) {
                    case "c"://買えるカードを表示
                        getCard();
                        break;
                    case "i"://買えるアイテムを表示
                        getItem();
                        break;
                    case "r"://デッキのカードを削除
                        removeCard();
                        break;
                    default:
                        System.out.println("店主「それはできないよ、選びなおしておくれ！」");
                        break;
                }
            }
            if (a == 1) {
                break;
            }
        }

    }

    public static void getCard() {
        System.out.println("どのカードを買う？");

        int b = 0;
        for (Integer integer : Player.cardList) {
            Player.c = new Card(integer);
            System.out.println("(" + b + ")" + Player.c.Text());
            b++;
        }
        System.out.println("(r)別のカードが欲しい！（ 5G で品揃えを更新）");
        System.out.println("(e)やめる");
        String str = input.next();
        try {
            if (str.equals("0") || str.equals("1") || str.equals("2")) {
                Player.getDeck().addCard(Player.cardList.get(Integer.parseInt(str)));
                System.out.println("カードを購入した");
                Player.setMoney(Player.getMoney() - 20);
                Player.cardList.remove(Integer.parseInt(str));
                        
            } else if (str.equals("r")) {
                //購入するカードの再取得
                Player.cardList.clear();
                for (int i = 0; i < 3; i++) {
                    int num = r.nextInt(Player.RandomCardList.size());
                    Player.cardList.add(Player.RandomCardList.get(num));
                }
                Player.setMoney(Player.getMoney() - 5);
                System.out.println("店主「商品（カード）を入れ替えたよ！」");
            } else if (str.equals("e")) {
                //  カードを買わずに終了
            } else {
                System.out.println("店主「それはできないよ、選びなおしておくれ！」");
            }
        } catch (Exception e) {
        }
        System.out.println("\n---------------------------------------------------------------\n");
    }

    public static void getItem() {
        System.out.println("<アイテム>");
        System.out.println("(1)HPポーション　|　HPを30回復（所持数：" + Player.getItems()[0] + "）");
        System.out.println("(2)MPポーション　|　MPを3回復（所持数：" + Player.getItems()[1] + "）");
        System.out.println("(3)リカバーハーブ　|　状態異常を回復（所持数：" + Player.getItems()[2] + "）");
        System.out.println("(e)やめる");
        System.out.println("どれにする？");
        String n = input.next();
        switch (n) {
            case "1"://HPポーション
                Player.getItems()[0]++;
                Player.setMoney(Player.getMoney() - 20);
                break;
            case "2"://MPポーション
                Player.getItems()[1]++;
                Player.setMoney(Player.getMoney() - 20);
                break;
            case "3"://リカバーハーブ
                Player.getItems()[2]++;
                Player.setMoney(Player.getMoney() - 20);
                break;
            case "e":
                break;
            default:
                System.out.println("店主「それはできないよ、選びなおしておくれ！」");
                break;
        }
        System.out.println("\n---------------------------------------------------------------\n");
    }

    public static void removeCard() {
        List<Card> cards = Player.getDeckList();
        System.out.println("<デッキリスト>");
        int a = 0;
        for (Card card : cards) {
            System.out.println("(" + a + ")" + card.Text());
            a++;
        }
        System.out.println("(e)やめる");
        System.out.println("削除したいカード番号は？");

        String n = input.next();

        if (n.equals("e")) {
            System.out.println("カードの削除をやめた");
        } else {
            try {
                int num = Integer.parseInt(n);
                Player.getDeck().removeCard(num);
                System.out.println("削除完了！");
                Player.setMoney(Player.getMoney() - 20);

            } catch (Exception e) {
                System.out.println("店主「それはできないよ、選びなおしておくれ！」");
            }
        }
        System.out.println("\n---------------------------------------------------------------\n");
    }
}
