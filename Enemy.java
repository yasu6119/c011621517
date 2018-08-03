
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import jdk.nashorn.internal.codegen.CompilerConstants;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author幡野
 */
public class Enemy {

    private static int hp; //現在のHP
    private static int maxHP;//最大HP
    private static int CP;//現在のCP
    private static int maxCP;//最大CP
    private static int maxHand;//手札の枚数
    private static int mp;
    private static int[] badStatus = new int[7];
    private static int armor;
    private static int level;
    private static int exp;
    private static Deck deck;
    private static Deck BattleDeck;
    private static List<Card> hand = new ArrayList<Card>();
    private static String[] str;
    private static List<Integer> effect = new ArrayList<Integer>();
    private static int money;
    private static String name;
    private static int weakPoint;
    private static Scanner input = new Scanner(System.in);
    private static int isFirstTurn;
    private static int playCount = 0;

    public static void selectEnemy(int enemyLevel) {//敵のステータスを決定する
        Random r = new Random();
        try {

            File file = new File("enemyStatus.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            int a = r.nextInt(2) + (enemyLevel * 2);

            String aaaString = new String();
            for (int i = 0; i < a + 2; i++) {
                aaaString = br.readLine();
            }

            str = aaaString.split(",");

            maxHP = Integer.parseInt(str[0]);
            hp = maxHP;
            maxCP = Integer.parseInt(str[1]);
            CP = maxCP;
            maxHand = Integer.parseInt(str[2]);
            mp = Integer.parseInt(str[3]);
            name = str[4];
            armor = 0;
            level = enemyLevel;
            hand.removeAll(hand);
            for (int i = 0; i < badStatus.length; i++) {
                badStatus[i] = 0;
            }

            String[] strDeck = str[5].split("/");

            weakPoint = Integer.parseInt(str[6]);
            isFirstTurn = Integer.parseInt(str[7]);
            int[] startDeck = new int[strDeck.length];

            for (int i = 0; i < strDeck.length; i++) {
                startDeck[i] = Integer.parseInt(strDeck[i]);
            }

            deck = new Deck(startDeck);
            List<Card> l = getDeck().getDeck();

            System.out.println(name);
        } catch (Exception e) {
        }
    }

    public static void setup() {//ターン開始時の処理
        playCount = 0;
        if (badStatus[0] >= 1) {
            hp -= badStatus[0];
        }

    }

    public static void cleanUP() {//ターン終了時の処理
        resetHand();
        playCount = 0;
        CP = maxCP + badStatus[5] - badStatus[6];
        armor += badStatus[4];
        for (int i = 0; i < badStatus.length; i++) {
            if (badStatus[i] >= 1) {
                badStatus[i]--;
            }

        }
    }

    public static void battleStatus() {
        System.out.println("<" + name + ">\n" + "HP:" + hp + "/" + maxHP + " CP:" + CP + "/" + maxCP + " MP:" + mp + "装甲:" + armor + "\n"
                + "-状態異常-");
        boolean a = false;
        for (int i = 0; i < badStatus.length; i++) {
            if (badStatus[i] != 0) {
                switch (i) {
                    case 0:
                        System.out.println("出血：LV" + badStatus[i] + "(ターン開始時LV分ダメージ)");
                        break;
                    case 1:
                        System.out.println("毒：LV" + badStatus[i] + "(カードをプレイするたび1ダメージ)");
                        break;
                    case 2:
                        System.out.println("延焼：LV" + badStatus[i] + "(LV分受けるダメージが増える)");
                        break;
                    case 3:
                        System.out.println("衰弱：LV" + badStatus[i] + "(LV分与えるダメージが増える)");
                        break;
                    case 4:
                        System.out.println("頑強：LV" + badStatus[i] + "(LV分得る装甲が増える)");
                        break;
                    case 5:
                        System.out.println("援助：LV" + badStatus[i] + "(LV分最大CPが増える)");
                        break;
                    case 6:
                        System.out.println("暗黒：LV" + badStatus[i] + "(LV分最大CPが減る)");
                        break;
                }
                a = true;
            }
        }
        if (a == false) {
            System.out.println("無し");
        }
        System.out.println("");
        for (int ef : effect) {
            try {
                File file = new File("cardList.csv");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String effectString = "";
                for (int i = 0; i < ef + 2; i++) {
                    effectString = br.readLine();
                }
                System.out.println(effectString);
            } catch (Exception e) {
            }
        }
    }

    public static void damage(int damage) {
        if (damage <= armor) {
            armor -= damage;
        } else {
            damage -= armor;
            armor = 0;
            hp -= damage;
        }
    }

    public static void draw(int number) {
        for (int i = 0; i < number; i++) {
            hand.add(deck.Draw());
        }

    }

    public static void resetHand() {

        hand.removeAll(hand);
        draw(maxHand);
    }

    public static void addBadStatus(int badStatus, int level) {
        Enemy.badStatus[badStatus] += level;
    }

    public static void heal(int heal) {
        hp += heal;
    }

    public static void playCard(int handNumber) {//カードを使う処理
        Card activeCard = hand.get(handNumber);
        Scanner input = new Scanner(System.in);
        boolean isCanPlaying = true;

        switch (activeCard.kind) {
            case 2:
                if (activeCard.cost > mp) {
                    isCanPlaying = false;

                } else {
                    mp -= activeCard.cost;
                }
                break;
            default:
                if (activeCard.cost > CP) {
                    isCanPlaying = false;

                } else {

                    CP -= activeCard.cost;
                }
                break;
        }
        if (isCanPlaying) {

            Battle.isPlayCard = true;
            hand.remove(handNumber);
            int damage = activeCard.damage - Enemy.getBadStatus()[3] + Player.getBadStatus()[2];;
            System.out.println(name + "の" + activeCard.name + "!");

            if (damage > 0) {
                System.out.println(damage + "ダメージを受けた!");

                Player.damage(damage);
            }

            int additionArmor = activeCard.armor;
            if (activeCard.armor > 0) {
                System.out.println(name + "は" + additionArmor + "の装甲を得た!");
                armor += additionArmor;
            }
            activeCard.cardEffect(activeCard.effect, activeCard.effectLevel, 1);
            activeCard.cardEffect(activeCard.effect2, activeCard.effectLevel2, 1);
            if (badStatus[1] > 0) {//毒 
                System.out.println("毒ダメージ");
                hp--;
            }
            playCount++;
            input.nextLine();
        }
    }

    public static int getPlayCount() {
        return playCount;
    }

    public static int getHp() {
        return hp;
    }

    public static int getMaxHP() {
        return maxHP;
    }

    public static int getCP() {
        return CP;
    }

    public static int getMaxCP() {
        return maxCP;
    }

    public static int getMaxHand() {
        return maxHand;
    }

    public static int getMp() {
        return mp;
    }

    public static int[] getBadStatus() {
        return badStatus;
    }

    public static int getArmor() {
        return armor;
    }

    public static int getLevel() {
        return level;
    }

    public static int getExp() {
        return exp;
    }

    public static Deck getDeck() {
        return deck;
    }

    public static Deck getBattleDeck() {
        return BattleDeck;
    }

    public static List<Card> getHand() {
        return hand;
    }

    public static String[] getStr() {
        return str;
    }

    public static List<Integer> getEffect() {
        return effect;
    }

    public static int getMoney() {
        return money;
    }

    public static String getName() {
        return name;
    }

    public static int getWeakPoint() {
        return weakPoint;
    }

    public static int getIsFirstTurn() {
        return isFirstTurn;
    }

    public static void setHp(int hp) {
        Enemy.hp = hp;
    }

    public static void setMaxHP(int maxHP) {
        Enemy.maxHP = maxHP;
    }

    public static void setCP(int CP) {
        Enemy.CP = CP;
    }

    public static void setMaxCP(int maxCP) {
        Enemy.maxCP = maxCP;
    }

    public static void setMaxHand(int maxHand) {
        Enemy.maxHand = maxHand;
    }

    public static void setMp(int mp) {
        Enemy.mp = mp;
    }

    public static void setBadStatus(int[] badStatus) {
        Enemy.badStatus = badStatus;
    }

    public static void setArmor(int armor) {
        Enemy.armor = armor;
    }

    public static void setLevel(int level) {
        Enemy.level = level;
    }

    public static void setExp(int exp) {
        Enemy.exp = exp;
    }

    public static void setDeck(Deck deck) {
        Enemy.deck = deck;
    }

    public static void setBattleDeck(Deck BattleDeck) {
        Enemy.BattleDeck = BattleDeck;
    }

    public static void setHand(List<Card> hand) {
        Enemy.hand = hand;
    }

    public static void setStr(String[] str) {
        Enemy.str = str;
    }

    public static void setEffect(List<Integer> effect) {
        Enemy.effect = effect;
    }

    public static void setMoney(int money) {
        Enemy.money = money;
    }

    public static void setName(String name) {
        Enemy.name = name;
    }

    public static void setWeakPoint(int weakPoint) {
        Enemy.weakPoint = weakPoint;
    }

    public static void setInput(Scanner input) {
        Enemy.input = input;
    }

}
