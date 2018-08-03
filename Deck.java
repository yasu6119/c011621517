
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 *
 * @author yasu
 */
public class Deck {
public  int cardID;
    List<Card> cardList = new ArrayList<Card>();
    private int top = 0;//デッキの一番上の位置を返す

    public Deck(int[] d) {//int型の配列を受け取りデッキを作成
        cardID=0;
        for (int i = 0; i < d.length; i++) {
            addCard(d[i]);
        }
    }

    public Card Draw() {//カードを引く
        //System.out.println("size"+deck.size());
        if (top >= cardList.size()) {
            reset();
           
        }
        Card d = cardList.get(top);
        
        top++;
        return d;
    }

    public void reset() {//デッキの補充
        top = 0;
        Collections.shuffle(cardList);
    }

    public void addCard(int c) {//引数のIDのカードを追加
        cardList.add(new Card(c,cardID));
        cardID++;
        Collections.shuffle(cardList);
    }

    public void removeCard(int c) {//cardList内の引数のインデックスのカードを削除
        cardList.remove(c);
        Collections.shuffle(cardList);
    }

    public List getDeck() {//cardListを返す

        return cardList;
    }

    public int getDeckNumber() {//残りデッキ枚数
        return cardList.size()-top;
    }

    public int getTop() {
        return top;
    }
    
}
