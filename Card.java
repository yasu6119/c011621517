
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
 * @author 幡野　大河内　前田　樋口
 */
public class Card {

    int id;
    int no;
    int kind;
    int damage;
    int armor;
    int effect;
    int effectLevel;
    int effect2;
    int effectLevel2;
    int cost;
    int aatribute;
    String text;
    String name;
    String[] str = new String[11];

    public Card(int no) {//コンストラクタ

        try {
            File file = new File("cardList.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int i = 0; i < no + 2; i++) {
                str = br.readLine().split(",");
            }

            this.no = Integer.parseInt(str[0]);
            name = str[1];
            damage = Integer.parseInt(str[2]);
            armor = Integer.parseInt(str[3]);
            effect = Integer.parseInt(str[4]);
            effectLevel = Integer.parseInt(str[5]);
            cost = Integer.parseInt(str[6]);
            aatribute = Integer.parseInt(str[7]);
            text = str[8];
            kind = Integer.parseInt(str[9]);
            effect2 = Integer.parseInt(str[11]);
            effectLevel2 = Integer.parseInt(str[12]);
        } catch (Exception e) {
        }
        Random r = new Random();
        int a = r.nextInt(2);

    }

    public Card(int c, int id) {
        this(c);
        this.id = id;
    }

    public String Text() {//カードテキストの表示
        String type = "";
        switch (kind) {
            case 1:
                type = "物理";
                break;
            case 2:
                type = "魔法";
                break;
            case 3:
                type = "特殊"
                        + "";
                break;
        }

        return "[" + name + "]" + type + ",コスト" + cost + "|" + text;
    }

    public void cardEffect(int effect, int effectLevel, int target) {//カードごとの処理を記載
        Scanner input = new Scanner(System.in);
        switch (effect) {
            case 1://ドロー
                System.out.println(effectLevel + "枚ドロー");
                if (target == 0) {
                    Player.draw(effectLevel);
                } else {
                    Enemy.draw(effectLevel);
                }
                break;
            case 2://HP回復
                if (target == 0) {
                    System.out.println("自分は" + effectLevel + "回復");
                    Player.heal(effectLevel);
                } else {
                    System.out.println("敵は" + effectLevel + "回復");
                    Enemy.heal(effectLevel);
                }
                break;
            case 3://出血
                if (target == 0) {
                    System.out.println(effectLevel + "レベルの出血を与えた");
                    Enemy.addBadStatus(0, effectLevel);
                } else {
                    System.out.println(effectLevel + "レベルの出血を受けた");
                    Player.addBadStatus(0, effectLevel);
                }
                break;
            case 4://CP回復
                if (target == 0) {
                    System.out.println("CPが" + effectLevel + "増加した");
                    Player.setCP(Player.getCP() + effectLevel);
                } else {
                    Enemy.setCP(Enemy.getCP() + effectLevel);
                }

                break;
            case 5://複数回攻撃
                if (target == 0) {
                    for (int i = 0; i < effectLevel; i++) {

                        System.out.println(damage + "ダメージ！");
                        Enemy.damage(calcDamage(target, damage));
                    }

                } else {
                    for (int i = 0; i < effectLevel; i++) {

                        System.out.println(damage + "ダメージ！");
                        Player.damage(calcDamage(target, damage));
                    }
                }
                break;
            case 6://毒

                if (target == 0) {
                    System.out.println(effectLevel + "レベルの毒を与えた");

                    Enemy.addBadStatus(1, effectLevel);
                } else {
                    System.out.println(effectLevel + "レベルの毒を受けた");
                    Player.addBadStatus(1, effectLevel);
                }
                break;
            case 7://CP減少
                System.out.println("CPが" + effectLevel + "減少した");
                if (target == 0) {
                    Enemy.setCP(Enemy.getCP() - effectLevel);
                } else {
                    Player.setCP(Player.getCP() - effectLevel);
                }
                break;
            case 8://強奪
                Random r = new Random();

                if (target == 0) {
                    int a = r.nextInt(Enemy.getHand().size());
                    Card c = Enemy.getHand().get(a);
                    Enemy.getHand().remove(a);
                    System.out.println(c.name + "を盗んだ");
                    Player.getHand().add(c);
                    Enemy.draw(1);

                } else {
                    int a = r.nextInt(Player.getHand().size());
                    Card c = Player.getHand().get(a);
                    Player.getHand().remove(a);
                    Enemy.getHand().add(c);
                    System.out.println(c.name + "を盗まれた");
                    Player.draw(1);
                }
                break;
            case 9://MP増加
                System.out.println("MPを" + effectLevel + "回復");
                if (target == 0) {

                    Player.setMp(Player.getMp() + effectLevel);
                } else {
                    Enemy.setMp(Enemy.getMp() + effectLevel);
                }
                break;
            case 10://手札を捨てさせる
                if (target == 0) {
                    Random r1 = new Random();
                    int a = r1.nextInt(Enemy.getHand().size());
                    Enemy.getHand().remove(a);
                } else {
                    System.out.println("手札を1枚選んで捨ててください");
                    Player.battleHand();
                    while (true) {
                        String string = input.next();
                        try {
                            Player.getHand().remove(Integer.parseInt(string));
                            break;
                        } catch (Exception e) {
                        }
                    }

                    break;
                }
                break;
            case 11://割合ダメージ

                if (target == 0) {
                    Enemy.setHp(Enemy.getHp() * effectLevel / 100);
                } else {
                    System.out.println("HPが" + effectLevel + "％減少");
                    Player.setHp(Player.getHp() * effectLevel / 100);
                }
                break;
            case 12://減っているHP分ダメージ
                if (target == 0) {
                    int damage = (Player.getMaxHP() - Player.getHp()) * effectLevel / 100 - Player.getBadStatus()[3] + Enemy.getBadStatus()[2];
                    Enemy.damage(damage);
                    System.out.println(damage + "ダメージ");
                } else {
                    int damage = (Enemy.getMaxHP() - Enemy.getHp()) * effectLevel / 100 - Enemy.getBadStatus()[3] + Player.getBadStatus()[2];
                    Player.damage((Player.getMaxHP() - Player.getHp()) * effectLevel / 100);
                    System.out.println(damage + "ダメージ");
                }

                break;
            case 13://自傷
                if (target == 0) {
                    System.out.println(effectLevel + "ダメージを受けた");
                    Player.damage(effectLevel);
                } else {
                    Enemy.damage(effectLevel);
                }
                break;
            case 14://装甲貫通ダメージ
                System.out.println("装甲貫通！" + effectLevel + "ダメージ！");
                if (target == 0) {
                    int damage = calcDamage(target, effectLevel);
                    Enemy.setHp(Enemy.getHp() - damage);
                } else {
                    int damage = calcDamage(target, effectLevel);
                    Player.setHp(Player.getHp() - damage);
                }
                break;
            case 15://延焼
                if (target == 0) {
                    System.out.println(effectLevel + "レベルの延焼を与えた");
                    Enemy.getBadStatus()[2] += effectLevel;
                } else {
                    System.out.println(effectLevel + "レベルの延焼を受けた");
                    Player.getBadStatus()[2] += effectLevel;
                }
                break;
            case 16://衰弱
                if (target == 0) {
                    System.out.println(effectLevel + "レベルの衰弱を与えた");
                    Enemy.getBadStatus()[3] += effectLevel;
                } else {
                    System.out.println(effectLevel + "レベルの衰弱を受けた");
                    Player.getBadStatus()[3] += effectLevel;
                }
                break;
            case 17://頑強
                System.out.println(effectLevel + "レベルの頑強を得た");
                if (target == 0) {

                    Player.getBadStatus()[4] += effectLevel;
                } else {
                    Enemy.getBadStatus()[4] += effectLevel;
                }
                break;
            case 18://援助
                System.out.println(effectLevel + "レベルの援助を得た");
                if (target == 0) {
                    Player.getBadStatus()[5] += effectLevel;
                } else {
                    Enemy.getBadStatus()[5] += effectLevel;
                }
                break;
            case 19://暗黒
                if (target == 0) {
                    System.out.println(effectLevel + "レベルの暗黒を与えた");
                    Enemy.getBadStatus()[6] += effectLevel;
                } else {
                    System.out.println(effectLevel + "レベルの暗黒を受けた");
                    Player.getBadStatus()[6] += effectLevel;
                }
                break;
            case 20://装甲分ダメージ
                if (target == 0) {
                    int damage = calcDamage(target, Player.getArmor());
                    System.out.println(damage + "ダメージ");
                    Enemy.damage(damage);
                } else {
                    int damage = calcDamage(target, Enemy.getArmor());
                    System.out.println(damage + "ダメージ");
                    Player.damage(damage);
                }
                break;
            case 21://手札を捨ててダメージ
                if (target == 0) {
                    int damage = calcDamage(target, Player.getHand().size() * effectLevel);
                    System.out.println(damage + "ダメージ");
                    Player.getHand().removeAll(Player.getHand());
                    Enemy.damage(damage);
                } else {
                    int damage = calcDamage(target, Enemy.getHand().size() * effectLevel);
                    System.out.println(damage + "ダメージ");
                    Enemy.getHand().removeAll(Enemy.getHand());
                    Player.damage(damage);
                }
                break;
            case 22://生ける剣
                if (target == 0) {
                    System.out.println("生ける剣が成長していく...");
                    this.damage += 2;
                    text = damage + "ダメージ　このカードのダメージを永続的に2上げる";
                    updateCard();
                }
                break;
            case 23://ゴールドを得る
                if (target == 0) {
                    Player.setMoney(Player.getMoney() + effectLevel);
                    System.out.println("どこからともなくお金が降ってきた");
                    System.out.println(effectLevel + "Gを得た");
                } else {
                    System.out.println("何も起こらなかった");
                }
                break;
            case 24://相手に装甲を与える
                if (target == 0) {
                    System.out.println("敵は" + effectLevel + "の装甲を得た");
                    Enemy.setArmor(Enemy.getArmor() + effectLevel);
                } else {
                    System.out.println("自分は" + effectLevel + "の装甲を得た");
                    Player.setArmor(Player.getArmor() + effectLevel);
                }
                break;
            case 25://デッキ枚数分ダメージ
                if (target == 0) {
                    int damage = calcDamage(target, Player.getDeck().getDeckNumber());
                    System.out.println(damage + "ダメージ！");
                    Enemy.damage(damage);
                } else {
                    int damage = calcDamage(target, Enemy.getDeck().getDeckNumber());
                    System.out.println(damage + "ダメージ！");
                    Player.damage(damage);
                }
                break;
            case 26://呪い
                if (target == 0) {
                    System.out.println("敵のデッキに呪いを" + effectLevel + "枚混ぜた");
                    for (int i = 0; i < effectLevel; i++) {
                        Enemy.getDeck().addCard(29);
                    }

                } else {
                    System.out.println("何も起こらなかった");
                }
                break;
            case 27://相手の手札のカードをコピー
                Random r1 = new Random();
                if (target == 0) {

                    int a = r1.nextInt(Enemy.getHand().size());
                    Card c = Enemy.getHand().get(a);
                    System.out.println(c.name + "をコピーした");
                    Player.getHand().add(c);

                } else {
                    int a = r1.nextInt(Player.getHand().size());
                    Card c = Player.getHand().get(a);

                    Enemy.getHand().add(c);
                    System.out.println(c.name + "をコピーされた");

                }
                break;
            case 28://ムラマサ
                if (target == 0) {
                    if (Enemy.getHp() <= 0) {
                        System.out.println("敵の血を吸い怨霊の刀は妖刀-ムラマサへと変貌した");
                        name = "妖刀-ムラマサ";
                        damage = 20;
                        text = "20ダメージ　自分はHPを10回復する";

                        this.effect = 2;
                        this.effectLevel = 10;
                        List<Card> aDeck = Player.getDeckList();
                        updateCard();
                    }

                } else {

                }
                break;

            case 29: //リロード 手札を引き直す
                System.out.println("");
                if (target == 0) {
                    int beforeHandSize = Player.getHand().size();
                    Player.getHand().removeAll(Player.getHand());
                    Player.draw(beforeHandSize + 1);
                } else {
                    int beforeHandSize = Enemy.getHand().size();
                    Enemy.getHand().removeAll(Enemy.getHand());
                    Enemy.draw(beforeHandSize + 1);
                }
                break;
            case 30://死霊の蠢き 墓地の枚数分ダメージ
                if (target == 0) {
                    int ghost = Player.getDeck().getTop();
                    System.out.println(ghost + "ダメージ!");
                    Enemy.damage(ghost);
                } else {
                    int ghost = Enemy.getDeck().getTop();
                    System.out.println(ghost + "ダメージ!");
                    Player.damage(ghost);
                }
                break;
            case 31://特殊カード全ハンデス
                if (target == 0) {
                    System.out.println("特殊カードをすべて捨てた");
                    for (int i = 0; i < Enemy.getHand().size(); i++) {
                        Card c = Enemy.getHand().get(i);
                        System.out.println("特殊カードをすべて捨てさせた");
                        if (c.kind == 3) {
                            Enemy.getHand().remove(i);
                        }
                    }

                } else {
                    for (int i = 0; i < Player.getHand().size(); i++) {
                        Card c = Player.getHand().get(i);
                        System.out.println("特殊カードをすべて捨てた");
                        if (c.kind == 3) {
                            Player.getHand().remove(i);
                        }
                    }
                }
                break;
            case 32://一心不乱
                if (target == 0) {
                    Player.draw(1);
                    Card activeCard = Player.getHand().get(Player.getHand().size() - 1);
                    System.out.println("めくったカードは" + activeCard.name + "だった");
                    Player.getHand().get(Player.getHand().size() - 1).cost = 0;
                    Player.playCard(Player.getHand().size() - 1);

                } else {
                    Enemy.draw(1);
                    Card activeCard = Enemy.getHand().get(Enemy.getHand().size() - 1);
                    System.out.println("めくったカードは" + activeCard.name + "だった");
                    Enemy.getHand().get(Enemy.getHand().size() - 1).cost = 0;
                    Enemy.playCard(Enemy.getHand().size() - 1);

                }
                break;
            case 33://墓地回収
                if (target == 0) {
                    Random random = new Random();
                    List<Card> list = Player.getDeckList();
                    for (int i = 0; i < effectLevel; i++) {
                        Card c = list.get(random.nextInt(Player.getDeck().getTop()));
                        System.out.println(c.name + "を墓地から回収した");
                        Player.getHand().add(c);
                    }
                } else {
                    Random random = new Random();
                    List<Card> list = Enemy.getDeck().getDeck();
                    for (int i = 0; i < effectLevel; i++) {
                        Card c = list.get(random.nextInt(Enemy.getDeck().getTop()));
                        System.out.println(c.name + "を墓地から回収した");
                        Enemy.getHand().add(c);
                    }
                }
                break;

            case 34://崩れゆく盾
                if (target == 0) {
                    if (this.armor > 0) {
                        System.out.println("盾が崩れ去っていく...");
                        this.armor -= 2;
                        text = armor + "装甲を得る　1枚カードを引く　使用するたび得られる装甲値が2減る";
                        updateCard();
                    }

                } else {
                    if (this.armor > 0) {
                        System.out.println("盾が崩れ去っていく...");
                        this.armor -= 2;
                        text = armor + "装甲を得る　1枚カードを引く　使用するたび得られる装甲値が2減る";
                        updateCard();
                    }

                }
                break;
            case 35://状態異常で追加ダメージ
                if (target == 0) {
                    if (Enemy.getBadStatus()[effectLevel2] > 0) {
                        System.out.println(effectLevel + "の追加ダメージ");
                        Enemy.damage(effectLevel);

                    }

                } else {

                    if (Player.getBadStatus()[effectLevel2] > 0) {
                        System.out.println(effectLevel + "の追加ダメージ");
                        Player.damage(effectLevel);

                    }

                }
                break;
            case 36://ナイフを手札に加える

                if (target == 0) {
                    System.out.println("ナイフを" + effectLevel + "枚手札に加えた");
                    for (int i = 0; i < effectLevel; i++) {

                        Player.getHand().add(new Card(30));
                    }
                } else {

                    for (int i = 0; i < effectLevel; i++) {
                        Enemy.getHand().add(new Card(30));
                    }

                }
                break;
            case 37://自分の手札を捨てる
                if (target == 0) {
                    System.out.println("手札を" + effectLevel + "枚選んで捨ててください");

                    for (int i = 0; i < effectLevel; i++) {
                        if (Player.getHand().size() == 0) {
                            break;
                        }
                        Player.battleHand();
                        while (true) {
                            String string = input.next();
                            try {
                                Player.getHand().remove(Integer.parseInt(string));
                                break;
                            } catch (Exception e) {
                            }
                        }

                    }

                } else {
                    Random r2 = new Random();
                    for (int i = 0; i < effectLevel; i++) {
                        if (Enemy.getHand().size() == 0) {
                            break;
                        }
                        int a = r2.nextInt(Enemy.getHand().size());
                        Enemy.getHand().remove(a);
                    }

                    break;
                }
                break;
            case 38://使用したカード分ダメージ

                if (target == 0) {
                    int damage = calcDamage(target, Player.getPlayCount() * effectLevel);
                    Enemy.damage(damage);
                      System.out.println(damage + "ダメージ！");
                } else {
                    int damage = calcDamage(target, Enemy.getPlayCount() * effectLevel);
                    Player.damage(damage);
                      System.out.println(damage + "ダメージ！");
                }
              
                break;
            case 39://最初に使ったら追加ダメージ
                if (target == 0) {
                    if (Player.getPlayCount() == 0) {
                        System.out.println(effectLevel + "の追加ダメージ");
                        Enemy.damage(effectLevel);
                    }
                } else {
                    if (Enemy.getPlayCount() == 0) {
                        System.out.println(effectLevel + "の追加ダメージ");
                        Player.damage(effectLevel);
                    }
                }
                break;

            case 40://素手攻撃ダメージアップ
                if (target == 0) {
                    System.out.println("こぶしのキレが"+effectLevel+"あがった！");
                    Battle.punchDamage += effectLevel;
                }
                break;
            case 41://敵の手札を見る
                if (target == 0) {
                    List<Card> hand = Enemy.getHand();
                    System.out.println("<敵の手札>");
                    for (Card c : hand) {
                        System.out.println(c.Text());
                    }
                }
                break;
            case 42://スーパースター
                if (target == 0) {
                    if (Player.getDeck().getTop() == Player.getDeckList().size()) {
                        System.out.println(effectLevel + "ダメージ！");
                        Enemy.damage(calcDamage(target, effectLevel));
                    } else {
                        System.out.println("しかし何も起こらなかった    ");
                    }
                } else {
                    if (Enemy.getDeck().getTop() == Enemy.getDeck().getDeck().size()) {
                        System.out.println(effectLevel + "ダメージ！");
                        Player.damage(calcDamage(target, effectLevel));
                    } else {
                        System.out.println("しかし何も起こらなかった    ");
                    }
                }
                break;
        }

    }

    private void updateCard() {//カード情報を上書きする
        List<Card> aDeck = Player.getDeckList();
        for (int i = 0; i < Player.getDeckList().size(); i++) {
            if (aDeck.get(i).id == id) {
                Player.getDeckList().set(i, this);
            }
        }
    }

    private int searchCard() {//同じIDのカードを探すしてアドレスを返す
        List<Card> aDeck = Player.getDeckList();
        for (int i = 0; i < Player.getDeckList().size(); i++) {
            if (aDeck.get(i).id == id) {
                return i;
            }
        }
        return 9999;
    }

    public int calcDamage(int target, int damage) {//実ダメージを計算する
        if (target == 0) {
            return damage - Player.getBadStatus()[3] + Enemy.getBadStatus()[2];
        } else {
            return damage - Enemy.getBadStatus()[3] + Player.getBadStatus()[2];
        }
    }

}
