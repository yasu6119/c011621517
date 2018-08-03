
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
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
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (true) {

            System.out.println("  ■    ■    ■  ■        ■■■■   ■■■■   ■■■■■    ■    ■   ■  ■■■■■  ■■■■  \n"
                    + "  ■    ■    ■  ■        ■   ■  ■   ■  ■        ■    ■  ■   ■      ■   ■ \n"
                    + "  ■   ■ ■   ■  ■        ■   ■  ■   ■  ■       ■ ■   ■ ■    ■      ■   ■ \n"
                    + "  ■   ■ ■   ■  ■        ■■■■   ■  ■■  ■■■■■   ■ ■   ■■■    ■■■■■  ■  ■■ \n"
                    + "  ■  ■   ■  ■  ■        ■   ■■ ■■■■   ■      ■   ■  ■ ■    ■      ■■■■  \n"
                    + "  ■  ■■■■■  ■  ■        ■    ■ ■  ■■  ■      ■■■■■  ■  ■   ■      ■  ■■ \n"
                    + "  ■  ■   ■  ■  ■        ■   ■■ ■   ■  ■      ■   ■  ■   ■  ■      ■   ■ \n"
                    + "■■  ■     ■ ■  ■■■■■    ■■■■■  ■   ■■ ■■■■■ ■     ■ ■   ■■ ■■■■■  ■   ■■");

            System.out.println("1→ゲームスタート\n"
                    + "2→ルール説明(ゲーム中いつでも参照できます)");

            String str1 = input.next();
            if (str1.equals("2")) {
                help();

            } else if (str1.equals("1")) {
                Player.gameStart();

                int result = Map.mainMap();

                //   int result = Battle.mainBattle(1 / 6);
                switch (result) {
                    case 1:
                        gameClear();
                        break;
                    case 2:
                        gameOver();
                        break;

                }
            } else if (str1.equals("test")) {
                int a = 0;
                Player.gameStart();
                while (a == 0) {

                    System.out.println("テストモード\n"
                            + "b→バトルテスト\n"
                            + "s→ショップテスト\n"
                            + "t→トレジャーテスト\n"
                            + "l→レベルを変える\n"
                            + "c→カード追加\n"
                            + "d→カード削除\n"
                            + "p→プレイヤーのステータス表示\n"
                            + "e→タイトルに戻る");
                    str1 = input.next();

                    switch (str1) {
                        case "b":
                            System.out.println("部屋番号？");
                            str1 = input.next();
                            int result = Battle.mainBattle((Integer.parseInt(str1) / 6));

                            Player.setHp(Player.getMaxHP());
                            Player.battleEnd();
                            Player.setExp(0);
                            break;

                        case "s":
                            Player.setMoney(999);
                            Shop.mainShop();
                            break;
                        case "t":
                            Tresure.mainTresure();
                            break;
                        case "p":
                            Player.battleStatus();
                            List<Card> l = Player.getDeckList();
                            for (Card card : l) {
                                System.out.println(card.Text());
                            }
                            break;
                        case "l":
                            System.out.println("変更するレベル？");
                            str1 = input.next();
                            Player.LevelUP(Integer.parseInt(str1));
                            break;
                        case "c":
                            int c = 0;
                            while (c == 0) {
                                System.out.println("追加するカード？");
                                str1 = input.next();
                                if (str1.equals("e")) {
                                    c = 1;
                                } else {
                                    Player.getDeck().addCard(Integer.parseInt(str1));
                                }
                            }

                            break;
                        case "d":
                            int b = 0;
                            List<Card> cards = Player.getDeckList();
                            while (b == 0) {
                                System.out.println("<デッキリスト>");
                                int a1 = 0;
                                for (Card card : cards) {
                                    System.out.println("(" + a1 + ")" + card.Text());
                                    a1++;
                                }
                                System.out.println("(e)やめる");
                                System.out.println("削除したいカード番号は？");

                                String n = input.next();

                                if (n.equals("e")) {
                                    System.out.println("カードの削除をやめた");
                                    b = 1;
                                } else {
                                    try {
                                        int num = Integer.parseInt(n);
                                        Player.getDeck().removeCard(num);
                                        System.out.println("削除完了！");

                                    } catch (Exception e) {
                                        System.out.println("店主「それはできないよ、選びなおしておくれ！」");
                                    }
                                }
                                System.out.println("\n---------------------------------------------------------------\n");
                            }
                            break;
                        case "e":
                            a = 1;
                            break;
                    }
                }

            }

        }

    }

    public static void gameClear() {
        Scanner input = new Scanner(System.in);
        System.out.println("           ■■■■    ■    ■■    ■■  ■■■■■       ■■■   ■     ■■■■■    ■    ■■■■         \n"
                + "          ■   ■■   ■    ■■    ■■  ■          ■   ■  ■     ■        ■    ■   ■        \n"
                + "         ■        ■ ■   ■■■  ■ ■  ■         ■       ■     ■       ■ ■   ■   ■        \n"
                + "         ■        ■ ■   ■ ■  ■ ■  ■■■■■     ■       ■     ■■■■■   ■ ■   ■  ■■        \n"
                + "■■■ ■■■  ■   ■■■ ■   ■  ■ ■  ■ ■  ■         ■       ■     ■      ■   ■  ■■■■  ■■■ ■■■\n"
                + "         ■     ■ ■■■■■  ■  ■■  ■  ■         ■       ■     ■      ■■■■■  ■  ■■        \n"
                + "          ■    ■ ■   ■  ■  ■■  ■  ■          ■   ■  ■     ■      ■   ■  ■   ■        \n"
                + "           ■■■■ ■     ■ ■      ■  ■■■■■       ■■■   ■■■■■ ■■■■■ ■     ■ ■   ■■       \n"
                + "Enterでタイトルに戻る");
        input.nextLine();

    }

    public static void gameOver() {
        Scanner input = new Scanner(System.in);
        System.out.println("           ■■■■    ■    ■■    ■■  ■■■■■       ■■■   ■     ■ ■■■■■  ■■■■         \n"
                + "          ■   ■■   ■    ■■    ■■  ■          ■   ■   ■   ■  ■      ■   ■        \n"
                + "         ■        ■ ■   ■■■  ■ ■  ■         ■     ■  ■   ■  ■      ■   ■        \n"
                + "         ■        ■ ■   ■ ■  ■ ■  ■■■■■     ■     ■  ■   ■  ■■■■■  ■  ■■        \n"
                + "■■■ ■■■  ■   ■■■ ■   ■  ■ ■  ■ ■  ■         ■     ■   ■ ■   ■      ■■■■  ■■■ ■■■\n"
                + "         ■     ■ ■■■■■  ■  ■■  ■  ■         ■     ■   ■ ■   ■      ■  ■■        \n"
                + "          ■    ■ ■   ■  ■  ■■  ■  ■          ■   ■    ■■    ■      ■   ■        \n"
                + "           ■■■■ ■     ■ ■      ■  ■■■■■       ■■■      ■    ■■■■■  ■   ■■       \n"
                + "Enterでタ イトルに戻る");
        input.nextLine();
    }

    public static void help() {
        Scanner input = new Scanner(System.in);
        int a = 0;
        while (a == 0) {
            System.out.println("<ヘルプ>\n"
                    + "項目を選んでください\n"
                    + "0→はじめに\n"
                    + "1→画面表示の説明\n"
                    + "2→マップのシステム\n"
                    + "3→バトルのシステム\n"
                    + "4→用語集\n"
                    + "5→戻る\n");
            String string = input.next();
            String fileString = new String();
            switch (string) {
                case "0":
                    fileString = "main.txt";
                    break;
                case "1":
                    fileString = "gamen.txt";
                    break;
                case "2":
                    fileString = "map.txt";
                    break;
                case "3":
                    fileString = "battle rule.txt";
                    break;
                case "4":
                    fileString = "word tips.txt";
                    break;

                case "5":
                    a = 1;
                    break;

            }
            try {
                File file = new File(fileString);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str = br.readLine();
                while (str != null) {
                    System.out.println(str);
                    str = br.readLine();
                }

            } catch (Exception e) {
            }

            System.out.println("------------------------------------------------------------------------------");
            input.nextLine();
        }

    }
}
