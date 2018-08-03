
import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javafx.scene.input.KeyCode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yasu
 */
public class Map {

    static int[] map = new int[25];
    static boolean[] mapHidden = new boolean[25];
    static boolean[] arrive = new boolean[25];
    static Scanner input = new Scanner(System.in);

    /*0~5 レベル1
      6~11 レベル2 
        12~171レベル3
    17~23レベル4
    24 boss
     */
    public static int mainMap() {
        setupMap();
       
        while (true) {
          
           
            showMap();
            Player.status();//プレイヤーのステータス表示

            System.out.println("d→デッキ表示\n"
                    + "i→所持アイテム表示\n"
                    + "h→ヘルプ\n"
                    + "部屋番号→その部屋に移動\n"
                    + "\n"
                    + "どうする...?");
            String n = input.next();
             System.out.println("\n---------------------------------------------------------------\n");
            switch (n) {
                case "d"://デッキリストの表示
                    List<Card> cards = Player.getDeckList();
                    System.out.println("<デッキリスト>");
                    for (Card card : cards) {

                        System.out.println(card.Text());
                    }
                    break;
                case "i"://アイテムリストの表示

                    System.out.println("<アイテムリスト>");
                    int[] item = Player.getItems();
                    if (item[0] != 0) {
                        System.out.println("HPポーション*" + item[0]);
                    }
                    if (item[1] != 0) {
                        System.out.println("MPポーション" + item[1]);
                    }
                    if (item[2] != 0) {
                        System.out.println("リカバーハーブ" + item[2]);
                    }
                    break;
                case "h":
                   Main.help();
                    
                    break;
                default://マップの移動
                    try {
                        int event = Integer.parseInt(n);

                        if (event < 0 || event > 24) {
                            System.out.println("その部屋は存在しない");
                        } else {
                            if (mapHidden[event] == false) {//マップの情報が開示されているか
                                System.out.println("その部屋は行くことができない");
                                input.nextLine();
                            } else if (arrive[event] == true && map[event] != 3) {//行ったことのある部屋かandショップでないか
                                System.out.println("その部屋にはもう行った");
                                input.nextLine();
                            } else {
                                move(event);
                             
                                switch (map[event]) {//部屋のイベントの処理
                                    case 1:
                                        Random r=new Random();
                                        int a=r.nextInt(2);
                                        int result = Battle.mainBattle((event / 6) );
                                        
                                        switch (result) {
                                            case 1://勝利
                                              

                                                break;
                                            case 2://ゲームオーバー

                                                return 2;
                                            case 3://ゲームクリア
                                                return 1;
                                        }
                                         map[event]=4;
                                        break;
                                    case 2:
                                        Tresure.mainTresure();
                                         map[event]=4;
                                        break;
                                    case 3:

                                        Shop.mainShop();
                                        break;
                                    case 4:
                                        break;
                                }
                              
                            }
                        }

                    } catch (Exception e) {
                    }
            }
           System.out.println("\n---------------------------------------------------------------\n"); 
        }
    }

    static public void setupMap() {//幡野
System.out.println("<マップ>");
        int[] lv1 = {4, 1, 1, 2, 4, 4};
        int[] lv2 = {1, 1, 2, 2, 3, 4};
        int[] lv3 = {1, 1, 2, 2, 4, 4};
        int[] lv4 = {1, 1, 2, 2, 4, 4};
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            list.add(i);
        }

        Collections.shuffle(list);
        for (int i = 0; i < lv1.length; i++) {
            map[i] = lv1[i];
        }

        Collections.shuffle(list);
        for (int i = 0; i < lv2.length; i++) {
            map[i + 6] = lv2[list.get(i)];
        }
        Collections.shuffle(list);
        for (int i = 0; i < lv3.length; i++) {
            map[i + 12] = lv3[list.get(i)];
        }
        Collections.shuffle(list);
        for (int i = 0; i < lv4.length; i++) {
            map[i + 18] = lv4[list.get(i)];
        }
        for (int i = 0; i < mapHidden.length; i++) {
            arrive[i] = false;
            mapHidden[i] = false;
        }

        mapHidden[0] = true;
        arrive[0] = true;
        mapHidden[1] = true;
        mapHidden[2] = true;
        map[24] = 1;
    }

  public static void showMap() { //樋口

        for (int i = 0; i <= 5; i += 2) {

            if (i == 0) {
                System.out.print("******************************************************************************************" + "\n");
            } else {
                System.out.print("********************************************************************************" + "\n");
            }

            if (i == 0) {
                System.out.print("*        **        **        **        **        **        **        **        **        *" + "\n");
            } else {
                System.out.print("*        **        **        **        **        **        **        **        *" + "\n");
            }

            for (int j = 0; j <= 18; j += 6) {
                for (int k = 0; k <= 1; k++) {

                    if (mapHidden[i + j + k] == false) {
                        System.out.print("*    ?   *");
                    }  else if(arrive[i+j+k] == true && map[i + j + k] != 3){
                        System.out.print("*        *");
                        
                    }else{
                        switch (map[i + j + k]) {
                            case 0: //部屋なし

                                break;

                            case 1: //敵とそのレベル
                                System.out.print("*   E");
                                if (i + j + k <= 5) {
                                    System.out.print("1");
                                } else if (i + j + k <= 11) {
                                    System.out.print("2");
                                } else if (i + j + k <= 17) {
                                    System.out.print("3");
                                } else if (i + j + k <= 23) {
                                    System.out.print("4");
                                }

                                System.out.print("   *");

                                break;

                            case 2: //宝
                                System.out.print("*    T   *");
                                break;

                            case 3: //店
                                System.out.print("*    S   *");
                                break;

                            case 4: //空部屋
                                System.out.print("*        *");
                                break;

                            default:

                                System.out.print("**********");

                        }
                    }

                }

            }

            if (i == 0) {
                System.out.print("*    B   *");
            }

            System.out.print("\n");

            //部屋番号の表示
            for (int j = 0; j <= 18; j += 6) {
                for (int k = 0; k <= 1; k++) {
                    System.out.print("*   ");

                    if (i + j + k <= 9 || mapHidden[i + j + k] == false || (arrive[i + j + k] == true && map[i+j+k] != 3)) {
                        System.out.print(" ");

                    } else if (i + j + k <= 19) {
                        System.out.print("1");
                    } else if (i + j + k <= 29) {
                        System.out.print("2");
                    }
                        
                        
                        
                    
                        if ( ( mapHidden[i + j + k] == false || arrive[i + j + k] == true ) && map[i+j+k] != 3) {
                            System.out.print(" ");
                        }else if(i + j + k <= 9 && mapHidden[i + j + k] == true){
                            System.out.print(i + j + k);
                        } else if (i + j + k <= 19 && mapHidden[i + j + k] == true) {
                            System.out.print(i + j + k - 10);
                        } else if (i + j + k <= 29 && mapHidden[i + j + k] == true) {
                            System.out.print(i + j + k - 20);
                        }

                        System.out.print("   *");

                    }
                }

                if (i == 0 && arrive[19] == false) {
                    System.out.print("*        *");
                } else if (i == 0 && arrive[19] == true) {
                    System.out.print("*   24   *");
                }

                System.out.print("\n");

                /*
            
            if (i == 0) {
                System.out.print("*        **        **        **        **        **        **        **        **        *" + "\n");
            } else {
                System.out.print("*        **        **        **        **        **        **        **        *" + "\n");
            }

                 */
                if (i == 0) {
                    System.out.print("******************************************************************************************" + "\n");
                } else {
                    System.out.print("********************************************************************************" + "\n");
                }

            }

            System.out.print("E(数字) = 敵(レベル) , T = 宝 " + "\n" + "S = ショップ , B = ボス" + "\n");

        }


    public static void move(int a) {//前田

        try {

            System.out.println(a + "の部屋に移動");
            input.nextLine();
            arrive[a] = true;
            //部屋の情報開示
            if (a % 6 <= 1) {
                if (a % 2 == 0) {
                    if (a != 0) {
                        mapHidden[a - 5] = true;
                    }
                    mapHidden[a + 1] = true;
                    mapHidden[a + 2] = true;

                } else {

                    mapHidden[a - 1] = true;
                    mapHidden[a + 5] = true;
                    mapHidden[a + 2] = true;
                }
            } else if (a % 6 <= 3) {

                if (a % 2 == 0) {
                    if (a != 2) {
                        mapHidden[a - 5] = true;

                    }

                    mapHidden[a + 1] = true;

                    mapHidden[a + 2] = true;

                    mapHidden[a - 2] = true;
                } else {

                    mapHidden[a - 1] = true;
                    if (a != 21) {
                        mapHidden[a + 5] = true;
                    }

                    mapHidden[a + 2] = true;
                    mapHidden[a - 2] = true;
                }
            } else {
                if (a % 2 == 0) {

                    if (a != 4) {
                        mapHidden[a - 5] = true;
                    }
                    mapHidden[a + 1] = true;
                    mapHidden[a - 2] = true;
                } else {

                    mapHidden[a - 1] = true;
                    if (a != 23) {
                        mapHidden[a + 5] = true;
                    }

                    mapHidden[a - 2] = true;
                }
            }

        } catch (Exception e) {
        }

    }

}
