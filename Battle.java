
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
public class Battle {

    public static boolean isPlayCard;
    public static boolean isLastBOSS = false;
    public static int punchDamage;

    public static int mainBattle(int enemyLV) {//バトル全体の処理
        isLastBOSS = false;
        punchDamage = 2;
        if (enemyLV == 4) {
            isLastBOSS = true;
        }
        Scanner input = new Scanner(System.in);
        Enemy.selectEnemy(enemyLV);

        Player.getDeck().reset();
        Enemy.getDeck().reset();
        Player.draw(Player.getMaxHand());
        Enemy.draw(Enemy.getMaxHand());
        System.out.println("\n\n>>>>>>>>>>" + Enemy.getName() + "が立ちふさがった<<<<<<<<<<\n");
        int result;
        Scanner input1 = new Scanner(System.in);

        input1.nextLine();
        while (true) {
            System.out.println("--------------------プレイヤーのターン--------------------");

            result = playerTurn();
            if (result > 0) {
                
                return result;
            }
            System.out.println("--------------------" + Enemy.getName() + "のターン--------------------");

            result = enemyTurn();
            if (result > 0) {

                return result;
            }

        }

    }

    public static int playerTurn() {//自分のターンの処理
        Player.setup();
        if (Player.getHp() <= 0) {//負け
            System.out.println("バトルに敗北した");
            return 2;
        }
        boolean IsPunched = false;
        Scanner input = new Scanner(System.in);
        while (true) {
            Enemy.battleStatus();
            Player.battleStatus();
            System.out.println("<手札>");
            Player.battleHand();
            System.out.println("\n手札の番号→カード使用\n"
                    + " e→ターン終了\n"
                    + " p→素手攻撃(1ターンに1回のみ)\n"
                    + " i→アイテム使用\n"
                    + " h→ヘルプ\n");
            String number = input.next();
            if (number.equals("e")) {//ターン終了
                Player.cleanUP();

                System.out.println("ターン終了");
                return 0;

            } else if (number.equals("p")) {//素手攻撃
                System.out.println("------------------------素手攻撃-------------------------");
                if (IsPunched) {
                    System.out.println("素手攻撃は1ターンに1度だ");
                } else {
                    if (Player.getCP() >= 1) {
                        IsPunched = true;
                        System.out.println("素手で攻撃！　" + punchDamage + "ダメージ");
                        Enemy.damage(punchDamage);

                        Player.setCP(Player.getCP() - 1);
                    } else {
                        System.out.println("CPが足りません");
                    }

                }
                input.nextLine();
            } else if (number.equals("i")) {
               System.out.println("-----------------------アイテム使用------------------------");
                item();
            } else if (number.equals("h")) {
                try {
                    Main.help();
                } catch (Exception e) {

                }

            } else {
                System.out.println("---------------------カードをプレイ---------------------");
                try {
                    int handNumber = Integer.parseInt(number);
                    Player.playCard(handNumber);
                } catch (Exception e) {
                }

            }
            //勝敗判定
            if (Enemy.getHp() <= 0) {//勝ち 
                System.out.println("バトルに勝利した");
                Player.reward(Enemy.getLevel());
                Player.battleEnd();
                if (isLastBOSS) {
                    return 3;
                }
                return 1;
            } else if (Player.getHp() <= 0) {//負け
                System.out.println("バトルに敗北した");
                return 2;
            }
           System.out.println("-------------------------------------------------------");
        }
    }

    public static int enemyTurn() {//相手ターンの処理
        Enemy.setup();
        if (Enemy.getHp() <= 0) {//出血死
              System.out.println("バトルに勝利した");
                Player.reward(Enemy.getLevel());
                Player.battleEnd();
            return 1;
        }
        while (true) {

            for (int i = 0; i < Enemy.getHand().size(); i++) {

                Enemy.playCard(i);

                //勝敗判定
                if (Enemy.getHp() <= 0) {//勝ち  
                    System.out.println("バトルに勝利した");
                    Player.reward(Enemy.getLevel());
                    Player.battleEnd();
                    if (isLastBOSS) {
                        return 3;
                    }
                    return 1;
                } else if (Player.getHp() <= 0) {//負け
                    System.out.println("バトルに敗北した");
                    return 2;
                }
            }
            if (isPlayCard == false) {
                Enemy.cleanUP();
                return 0;
            }
            isPlayCard = false;

        }
    }

    public static void item() {
        int b = 0;
        while (b == 0) {
            System.out.println("使うアイテムを選んでください");
            int[] item = Player.getItems();

            if (item[0] != 0) {
                System.out.println("(0)HPポーション　|　HPを30回復（所持数：" + Player.getItems()[0] + "）");
            }
            if (item[1] != 0) {
                System.out.println("(1)MPポーション　|　MPを3回復（所持数：" + Player.getItems()[1] + "）");
            }
            if (item[2] != 0) {
                System.out.println("(2)リカバーハーブ　|　状態異常を回復（所持数：" + Player.getItems()[2] + "）");
            }
            System.out.println("(e) 戻る");
            Scanner input = new Scanner(System.in);
            String number = input.next();
            try {
                switch (number) {
                    case "0":
                        if (item[0] != 0) {
                            Player.getItems()[0]--;
                            System.out.println("HPが30回復");
                            Player.heal(30);
                        }
                        break;

                    case "1":
                        if (item[1] != 0) {
                            Player.getItems()[1]--;
                            System.out.println("MPが3回復");
                            Player.setMp(Player.getMp() + 3);
                        }
                        break;
                    case "2":
                        if (item[2] != 0) {
                            Player.getItems()[2]--;
                            System.out.println("状態異常を回復");
                            Player.cleanBadStatus();
                        }
                        break;
                    case "e":
                        b = 1;
                }
            } catch (Exception e) {
            }

        }
    }
}
