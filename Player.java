
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
 * @author 幡野　大河内
 */
public class Player {

    private static int hp; //現在のHP
    private static int maxHP;//最大HP
    private static int CP;//現在のCP
    private static int maxCP;//最大CP
    private static int maxHand;//手札の枚数
    private static int mp;//現在のmp
    private static int[] badStatus = new int[7];
// 状態異常　badStatus[0]=出血LV　BadStatus[1]=毒LV badStatus[2]=延焼LV　BadStatus[3]=衰弱LV　badStatus[4]=頑強LV　BadStatus[5]=援助LV　badStatus[6]=暗黒LV 
    private static int armor;// 装甲の値
    private static int level;//プレイヤーのレベル
    private static int exp;//敵を倒した数
    private static int[] startDeck = {0, 0, 0, 0, 2, 2, 2, 3, 1};//初期デッキの内容
    private static Deck deck;//プレイヤーのデッキ
    //private static Deck BattleDeck;
    private static List<Card> hand = new ArrayList<Card>(); //手札のリスト
    private static String[] str;//作業用ファイル
    private static List<Integer> effect = new ArrayList<Integer>();//プレイヤーが受けた効果
    private static int money;//お金
    private static int[] items;//所持アイテムのリスト 1.HPポーション 2.MPポーション　3.リカバーハーブ
    public static List<Integer> RandomCardList = new ArrayList<Integer>();
    static List<Integer> cardList = new ArrayList<Integer>();
    static Card c;
    public static Random r = new Random();
    private static int playCount = 0; //そのターン中に使用したカードの枚数

    public Player() {

    }

    public static void gameStart() {//ゲーム開始時の初期化
        try {
            Battle.isLastBOSS = false;
            File file = new File("playerStatus.csv");
            File file2 = new File("cardList.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int i = 0; i < 2; i++) {
                str = br.readLine().split(",");
            }
            hand.removeAll(hand);
            Enemy.getHand().removeAll(hand);
            maxHP = Integer.parseInt(str[0]);
            hp = maxHP;
            maxCP = Integer.parseInt(str[1]);
            CP = maxCP;
            maxHand = Integer.parseInt(str[2]);
            mp = Integer.parseInt(str[3]);
            armor = 0;
            level = 1;
            items = new int[3];
            exp = 0;
            money = 0;

            for (int i = 0; i < badStatus.length; i++) {
                badStatus[i] = 0;
            }
            deck = new Deck(startDeck);
            br = new BufferedReader(new FileReader(file2));
            int a = 0;
            String string1
                    = br.readLine();
            while (str != null) {
                str = br.readLine().split(",");
                String string = str[10];

                int isEnemyCard = Integer.parseInt(string);

                if (isEnemyCard == 0) {
                    RandomCardList.add(a);

                }

                a++;
            }
            for (Integer integer : RandomCardList) {
                System.out.print(integer + ",");
            }
        } catch (Exception e) {

        }
        cardList.clear();
        for (int i = 0; i < 3; i++) {
            int num = r.nextInt(RandomCardList.size());
            cardList.add(RandomCardList.get(num));
        }
    }

    public static void setup() {//ターン開始時の処理
        playCount = 0;
        if (badStatus[0] >= 1) {
            hp -= badStatus[0];
            System.out.println("出血で" + badStatus[0] + "ダメージを受けた");
            if (hp <= 0) {

            }
        }

    }

    public static void addBadStatus(int badStatus, int level) {
        Player.badStatus[badStatus] += level;
    }

    public static void cleanUP() {//ターン終了時の処理
        resetHand();
        CP = maxCP + badStatus[5] - badStatus[6];
        if (badStatus[4]>0) {
            System.out.println(badStatus[4]+"の装甲を得た");
        }
        armor += badStatus[4];
        for (int i = 0; i < badStatus.length; i++) {
            if (badStatus[i] > 0) {
                badStatus[i]--;
            }
            
        }
    }

    public static void cleanBadStatus() {//状態異常を回復
        for (int i = 0; i < badStatus.length; i++) {
            if (i != 4 || i != 5) {
                badStatus[i] = 0;
            }

        }
    }

    public static void status() {//非戦闘時のステータス表示
        System.out.println("<ステータス>\n"
                + "HP:" + hp + "/" + maxHP + " MP:" + mp + " デッキ枚数" + deck.getDeckNumber() + "枚　お金" + money
                + "G\n");
    }

    public static void battleStatus() {//戦闘時のステータス表示
        System.out.println("<プレイヤー>\n" + "HP:" + hp + "/" + maxHP + " CP:" + CP + "/" + maxCP + " MP:" + mp + " 装甲:" + armor + " デッキ枚数:" + Player.getDeck().getDeckNumber() +" 素手攻撃ダメージ:" + Battle.punchDamage + "\n"
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
                        System.out.println("頑強：LV" + badStatus[i] + "(ターン終了時LV分装甲を得る)");
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
        /* for (int ef : effect) {
            try {
                File file = new File("EffectList.csv");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String effectString = "";
                for (int i = 0; i < ef + 2; i++) {
                    effectString = br.readLine();
                }
                System.out.println(effectString);
            } catch (Exception e) {
            }
        }*/
    }

    public static List getDeckList() {//デッキのカードリストを返す
        return deck.getDeck();
    }

    public static void battleHand() {//バトル時の手札の表示
        int a = 0;
        for (Card c : hand) {
            System.out.println("(" + a + ")" + c.Text());
            a++;
        }
    }

    public static void setMp(int mp) {
        Player.mp = mp;
    }

    public static void playCard(int handNumber) {//カードを使う処理
        Card activeCard = hand.get(handNumber);
        Scanner input = new Scanner(System.in);
        boolean isCanPlaying = true;

        switch (activeCard.kind) {
            case 2:
                if (activeCard.cost > mp) {
                    isCanPlaying = false;
                    System.out.println("MPが足りません");
                } else {
                    mp -= activeCard.cost;
                }
                break;
            default:
                if (activeCard.cost > CP) {
                    isCanPlaying = false;
                    System.out.println("CPが足りません");
                } else {
                    CP -= activeCard.cost;
                }
                break;
        }
        if (isCanPlaying) {

            hand.remove(handNumber);
            int damage = activeCard.damage;
            System.out.println(activeCard.name + "!");
            if (activeCard.aatribute == Enemy.getWeakPoint()) {
                damage *= 2;
                System.out.println("弱点を突いた！");
            }
            if (damage > 0) {

                System.out.println(activeCard.calcDamage(0, damage) + "ダメージ!");

                Enemy.damage(activeCard.calcDamage(0, damage));
            }

            int additionArmor = activeCard.armor;
            if (activeCard.armor > 0) {
                System.out.println(additionArmor + "の装甲を得た!");
                armor += additionArmor;
            }
            activeCard.cardEffect(activeCard.effect, activeCard.effectLevel, 0);
            activeCard.cardEffect(activeCard.effect2, activeCard.effectLevel2, 0);
            if (badStatus[1] > 0) {//毒
                System.out.println("毒ダメージ！");
                hp--;
            }
            playCount++;
            input.nextLine();
        }
    }

    public static void draw(int number) {//引数の値までカードを引く
        for (int i = 0; i < number; i++) {
            hand.add(deck.Draw());
        }

    }

    public static void resetHand() {
        hand.removeAll(hand);
        draw(maxHand);
    }

    public static void damage(int damage) {//引数の値だけダメージを受ける

        if (damage <= armor) {
            armor -= damage;
        } else {
            damage -= armor;
            armor = 0;
            hp -= damage;
        }
    }

    public static void heal(int heal) {
        hp += heal;
        if (maxHP < hp) {
            hp = maxHP;
        }
    }

    public static void reward(int enemyLV) {//戦闘報酬の獲得

        Scanner input = new Scanner(System.in);
       Random r=new Random();
        int a =Player.r.nextInt(15) +(enemyLV + 1) * 6;
        System.out.println(a + "ゴールドを得た");
        input.nextLine();
        money += a;
        System.out.println("敵は宝箱を落とした");
        input.nextLine();
        Tresure.mainTresure();
        exp++;
        if (exp % 2 == 0) {
            System.out.println("------------ L  E  V  E  L  U  P ！----------------");

            LevelUP(level + 1);
        }
    }

    public static void LevelUP(int level) {//レベルアップ
        Scanner inputScanner = new Scanner(System.in);

        try {
            File file = new File("17han data - playerStatus.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int i = 0; i <= Player.level + 1; i++) {
                str = br.readLine().split(",");
            }
            System.out.println("最大HP" + maxHP + "→" + Integer.parseInt(str[0]));
            maxHP = Integer.parseInt(str[0]);
            hp = maxHP;
            System.out.println("最大CP" + maxCP + "→" + Integer.parseInt(str[1]));
            maxCP = Integer.parseInt(str[1]);
            CP = maxCP;
            System.out.println("手札の最大数" + maxHand + "→" + Integer.parseInt(str[2]));
            maxHand = Integer.parseInt(str[2]);
            System.out.println("MPが回復");
            mp = Integer.parseInt(str[3]);
            armor = 0;
            Player.level = level;
            inputScanner.nextLine();
        } catch (Exception e) {

        }
    }

    public static void battleEnd() {//バトルが終わった時の処理
        cleanBadStatus();

        armor = 0;
        CP = maxCP;
        hand.removeAll(hand);
        deck.reset();
    }

    public static int getLevel() {
        return level;
    }

    public static int getExp() {
        return exp;
    }

    public static int getPlayCount() {
        return playCount;
    }

    public static int getMoney() {
        return money;
    }

    public static int getMaxHP() {
        return maxHP;
    }

    public static int getHp() {
        return hp;
    }

    public static int getMaxCP() {
        return maxCP;
    }

    public static int getCP() {
        return CP;
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

    public static List<Integer> getEffect() {
        return effect;
    }

    public static int[] getItems() {
        return items;
    }

    public static int[] getStartDeck() {
        return startDeck;
    }

    public static Deck getDeck() {
        return deck;
    }

    public static List<Card> getHand() {
        return hand;
    }

    public static void setHp(int hp) {
        Player.hp = hp;
    }

    public static void setMaxHP(int maxHP) {
        Player.maxHP = maxHP;
    }

    public static void setCP(int CP) {
        Player.CP = CP;
    }

    public static void setMaxCP(int maxCP) {
        Player.maxCP = maxCP;
    }

    public static void setMaxHand(int maxHand) {
        Player.maxHand = maxHand;
    }

    public static void addBadStatus(int[] badStatus) {
        Player.badStatus = badStatus;
    }

    public static void setArmor(int armor) {
        Player.armor = armor;
    }

    public static void setLevel(int level) {
        Player.level = level;
    }

    public static void setExp(int exp) {
        Player.exp = exp;
    }

    public static void setStartDeck(int[] startDeck) {
        Player.startDeck = startDeck;
    }

    public static void setDeck(Deck deck) {
        Player.deck = deck;
    }

    public static void setHand(List<Card> hand) {
        Player.hand = hand;
    }

    public static void setStr(String[] str) {
        Player.str = str;
    }

    public static void setEffect(List<Integer> effect) {
        Player.effect = effect;
    }

    public static void setMoney(int money) {
        Player.money = money;
    }

    public static void setItems(int[] items) {
        Player.items = items;
    }

}
