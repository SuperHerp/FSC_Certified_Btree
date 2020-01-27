import junit.framework.JUnit4TestAdapter;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;

public class BTree extends AbstractBTree {
    public BTree(int degree) {
        super(degree);
        //this.setRoot(new BTreeNode(degree));
    }

    @Override
    public boolean hasKey(int key) {
        AbstractBTreeNode curNode = this.getRoot();
        if(curNode == null){
            return false;
        }
        return curNode.hasKey(key);
    }

    @Override
    public void insert(int key) {
        System.out.println("BTree-----------------------------------------------------------------------");
        if(this.hasKey(key)){
            System.out.println("BTree contains key already");
            return;
        }
        if(this.getRoot() == null){
            this.setRoot(new BTreeNode(this.getDegree()));
        }
        AbstractBTreeNode curNode = this.getRoot();
        Comparator<Integer> cmp = Comparator.naturalOrder();
        Stack<AbstractBTreeNode> parentStack = new Stack<>();

        AbstractBTreeNode curNodeCP = curNode;
        while(true){
            int i;
            for(i = 0; i < curNodeCP.getKeys().size(); i++){
                if(curNodeCP.getKeys().get(i) > key){
                    break;
                }
            }
            parentStack.push(curNodeCP);
            if (i >= curNodeCP.getChildren().size()){
                break;
            }else{
                curNodeCP = curNodeCP.getChildren().get(i);
            }
        }
        curNode = curNodeCP;
        OverflowNode ovfl = curNode.insert(key);
        while(true) {
            if(ovfl == null){
                return;
            }else{
                parentStack.pop();
                if(parentStack.size() < 1){
                    if(parentStack.size() == 0) {
                        AbstractBTreeNode newRoot = new BTreeNode(curNode.getDegree());
                        newRoot.addKey(ovfl.getKey());
                        newRoot.addChild(curNode);
                        newRoot.addChild(ovfl.getRightChild());
                        this.setRoot(newRoot);
                        return;
                    }
                }else {
                    curNode = parentStack.peek();
                    ArrayList<Integer> keys = curNode.getKeys();
                    ArrayList<AbstractBTreeNode> children = curNode.getChildren();

                    key = ovfl.getKey();

                    keys.add(key);
                    keys.sort(cmp);

                    children.add(ovfl.getRightChild());
                    BTreeNode.sortAL(children);

                    if (keys.size() > 2 * curNode.getDegree()) {
                        ovfl = curNode.split();
                        continue;
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String toJson() {
        AbstractBTreeNode curNode = this.getRoot();
        if(curNode == null){
            String empty = "{}";
            return empty;
        }
        return curNode.toJson();
    }

    public static void main(String[] args) {

        int degree = 2;
        Random rand = new Random();
        int rnd;

        BTree bTree0 = new BTree(degree);

        AbstractBTreeNode root = new BTreeNode(degree);
        bTree0.setRoot(root);


        bTree0.insert(2);
        bTree0.insert(8);
        bTree0.insert(9);
        bTree0.insert(17);

        bTree0.insert(23);
        bTree0.insert(21);
        bTree0.insert(22);
        bTree0.insert(24);

        bTree0.insert(25);
        bTree0.insert(30);
        bTree0.insert(27);
        bTree0.insert(10);

        bTree0.insert(11);
        bTree0.insert(12);
        bTree0.insert(13);
        bTree0.insert(14);

        bTree0.insert(15);
        bTree0.insert(16);
        bTree0.insert(18);
        bTree0.insert(19);

        bTree0.insert(20);
        bTree0.insert(26);
        bTree0.insert(28);
        bTree0.insert(29);

        bTree0.insert(31);
        bTree0.insert(32);
        bTree0.insert(33);


        String expected = "{keys:[15,25],children:[{keys:[9,12],children:[{keys:[2,8]},{keys:[10,11]},{keys:[13,14]}]},{keys:[18,22],children:[{keys:[16,17]},{keys:[19,20,21]},{keys:[23,24]}]},{keys:[28,31],children:[{keys:[26,27]},{keys:[29,30]},{keys:[32,33]}]}]}";


        //String expected = "{keys:[15],children:[{keys:[9,12],children:[{keys:[2,8]},{keys:[10,11]},{keys:[13,14]}]},{keys:[22,25],children:[{keys:[17,21]},{keys:[23,24]},{keys:[27,30]}]}]}";

        String actual = bTree0.toJson();

        Assert.assertEquals("unequal", expected, actual);

        System.out.println(bTree0.toJson());


        /*--------------------------------------------------------------------------------------------------
        int[] stolenInts =  {-2173,-2169,-2166,-2163,-2161,-2159,-2158,-2153,-2150,-2145,-2140,-2135,-2133,-2130,-2127,-2123,-2119,-2117,-2116,-2115,-2112,-2108,-2107,-2103,-2099,-2094,-2089,-2085,-2080,-2079,-2074,-2073,-2069,-2067,-2066,-2065,-2060,-2057,-2054,-2052,-2050,-2045,-2040,-2037,-2035,-2033,-2028,-2026,-2022,-2019,-2016,-2015,-2014,-2012,-2008,-2003,-2002,-2001,-1996,-1994,-1992,-1991,-1989,-1987,-1983,-1981,-1977,-1972,-1967,-1966,-1965,-1960,-1956,-1953,-1952,-1949,-1948,-1943,-1942,-1939,-1936,-1934,-1931,-1926,-1921,-1920,-1918,-1914,-1913,-1911,-1906,-1902,-1900,-1898,-1894,-1890,-1888,-1883,-1880,-1879,-1878,-1873,-1871,-1869,-1864,-1861,-1859,-1856,-1854,-1853,-1850,-1848,-1843,-1839,-1837,-1836,-1833,-1828,-1826,-1824,-1823,-1822,-1821,-1818,-1813,-1810,-1808,-1806,-1802,-1798,-1796,-1793,-1792,-1789,-1786,-1785,-1782,-1780,-1778,-1777,-1776,-1772,-1769,-1765,-1763,-1761,-1760,-1757,-1752,-1747,-1742,-1741,-1740,-1737,-1732,-1727,-1726,-1725,-1723,-1720,-1719,-1715,-1711,-1710,-1709,-1705,-1702,-1698,-1695,-1693,-1692,-1689,-1684,-1683,-1679,-1676,-1671,-1669,-1666,-1662,-1657,-1654,-1649,-1645,-1642,-1637,-1636,-1633,-1630,-1627,-1625,-1621,-1620,-1619,-1616,-1612,-1610,-1607,-1603,-1599,-1595,-1591,-1590,-1586,-1584,-1581,-1579,-1578,-1573,-1570,-1566,-1565,-1562,-1560,-1555,-1550,-1545,-1542,-1539,-1538,-1536,-1533,-1529,-1527,-1522,-1521,-1519,-1514,-1510,-1506,-1503,-1499,-1494,-1490,-1487,-1485,-1484,-1483,-1478,-1474,-1470,-1469,-1468,-1464,-1461,-1460,-1458,-1453,-1452,-1449,-1445,-1443,-1441,-1440,-1437,-1435,-1434,-1432,-1429,-1427,-1426,-1424,-1420,-1416,-1412,-1411,-1407,-1404,-1401,-1399,-1395,-1391,-1388,-1387,-1383,-1380,-1379,-1375,-1374,-1373,-1371,-1369,-1368,-1363,-1360,-1359,-1356,-1355,-1352,-1347,-1345,-1344,-1341,-1340,-1337,-1332,-1327,-1324,-1323,-1318,-1317,-1316,-1314,-1312,-1308,-1303,-1302,-1297,-1293,-1290,-1287,-1282,-1280,-1276,-1275,-1272,-1270,-1266,-1261,-1256,-1254,-1253,-1252,-1249,-1246,-1241,-1237,-1232,-1228,-1225,-1223,-1220,-1218,-1217,-1214,-1211,-1207,-1202,-1201,-1200,-1198,-1194,-1193,-1188,-1187,-1185,-1184,-1180,-1177,-1172,-1170,-1165,-1163,-1159,-1155,-1150,-1146,-1145,-1144,-1139,-1138,-1133,-1130,-1127,-1122,-1120,-1116,-1114,-1109,-1105,-1100,-1096,-1091,-1090,-1086,-1085,-1080,-1075,-1072,-1068,-1064,-1063,-1058,-1056,-1051,-1049,-1046,-1044,-1042,-1037,-1032,-1029,-1025,-1023,-1018,-1013,-1012,-1009,-1008,-1005,-1002,-998,-994,-990,-989,-986,-985,-982,-978,-976,-971,-966,-965,-963,-959,-957,-953,-950,-947,-945,-940,-937,-936,-935,-931,-927,-924,-919,-915,-913,-910,-907,-902,-898,-896,-892,-888,-886,-884,-882,-881,-877,-876,-872,-870,-869,-866,-862,-861,-858,-856,-855,-854,-850,-848,-846,-841,-839,-836,-832,-831,-829,-828,-827,-824,-822,-819,-814,-809,-805,-801,-799,-795,-790,-786,-785,-783,-782,-778,-777,-775,-772,-771,-768,-765,-763,-758,-756,-751,-748,-747,-744,-741,-737,-733,-731,-729,-728,-725,-721,-717,-713,-712,-711,-710,-706,-701,-696,-693,-690,-686,-685,-682,-679,-675,-673,-670,-665,-660,-659,-658,-654,-650,-646,-645,-644,-643,-639,-637,-633,-628,-624,-622,-618,-616,-614,-609,-604,-603,-598,-597,-594,-590,-586,-582,-577,-572,-569,-566,-563,-558,-553,-548,-545,-542,-539,-538,-537,-534,-529,-528,-527,-524,-522,-519,-515,-514,-513,-510,-505,-500,-497,-495,-493,-490,-489,-484,-483,-482,-479,-478,-474,-472,-469,-468,-465,-462,-460,-458,-454,-450,-446,-442,-439,-438,-434,-431,-427,-423,-422,-418,-414,-410,-406,-405,-404,-400,-398,-397,-396,-395,-390,-389,-388,-383,-379,-375,-372,-367,-365,-360,-357,-353,-349,-345,-344,-341,-336,-331,-330,-329,-326,-321,-318,-316,-314,-309,-308,-303,-301,-297,-294,-289,-286,-283,-281,-279,-277,-274,-271,-270,-269,-266,-263,-260,-255,-254,-252,-250,-246,-241,-237,-234,-233,-231,-228,-225,-224,-221,-216,-212,-211,-207,-205,-200,-198,-196,-195,-194,-193,-191,-190,-188,-184,-179,-176,-173,-168,-164,-163,-160,-158,-153,-148,-143,-142,-139,-135,-132,-128,-127,-126,-121,-117,-114,-112,-107,-106,-104,-100,-98,-97,-94,-92,-89,-84,-80,-76,-74,-69,-67,-64,-60,-59,-57,-52,-48,-43,-39,-35,-30,-28,-23,-19,-15,-11,-10,-9,-8,-7,-3,2,6,7,9,11,16,17,22,25,30,34,39,42,46,50,55,56,58,63,66,70,74,75,80,84,86,89,93,98,103,104,105,106,108,110,113,117,118,123,128,129,131,136,138,142,144,149,150,155,159,164,167,169,173,175,180,183,185,188,191,196,197,202,205,209,210,212,216,220,222,223,226,231,234,236,238,243,245,250,252,255,257,262,265,269,271,272,277,282,287,291,293,297,300,304,309,314,318,323,327,332,337,340,343,345,347,350,355,358,363,368,373,375,379,381,382,386,389,391,393,397,399,403,407,408,411,415,417,421,426,429,430,432,433,436,438,443,446,449,454,459,464,468,469,470,473,477,479,481,482,483,486,487,491,495,496,497,499,504,507,509,512,515,518,519,520,523,525,526,529,530,531,534,537,541,543,547,548,551,554,556,557,560,562,564,569,570,574,578,581,583,585,589,590,594,598,602,606,611,613,617,618,619,621,625,630,632,637,642,643,648,649,653,655,657,659,662,667,671,676,677,678,681,684,688,693,698,699,704,707,711,716,720,722,724,728,729,730,732,735,740,745,746,747,749,752,756,758,763,767,770,771,773,774,776,778,781,786,790,793,797,798,801,803,808,810,811,813,816,819,821,823,824,827,832,833,838,840,841,846,847,851,853,858,859,860,861,863,868,871,874,877,880,881,885,886,888,890,893,895,900,905,906,907,912,917,921,925,928,931,934,936,938,939,940,942,943,947,952,955,960,965,970,973,974,979,983,984,988,991,995,996,999,1001,1006,1010,1014,1016,1018,1022,1023,1027,1031,1032,1034,1035,1036,1040,1044,1049,1051,1053,1057,1061,1064,1069,1071,1073,1075,1076,1080,1083,1084,1085,1089,1092,1095,1098,1103,1106,1108,1113,1116,1121,1125,1129,1131,1135,1139,1143,1145,1149,1154,1158,1159,1164,1167,1171,1174,1175,1179,1180,1181,1185,1190,1195,1197,1201,1204,1208,1211,1215,1220,1221,1223,1224,1225,1229,1230,1233,1235,1239,1243,1246,1247,1248,1253,1256,1258,1263,1264,1265,1267,1269,1273,1275,1280,1285,1288,1292,1294,1296,1298,1303,1308,1310,1312,1314,1317,1319,1320,1322,1325,1326,1329,1333,1335,1340,1343,1346,1347,1350,1355,1357,1361,1366,1371,1375,1377,1381,1382,1384,1385,1388,1393,1396,1401,1404,1405,1408,1411,1414,1418,1421,1424,1428,1432,1435,1438,1441,1443,1447,1448,1453,1455,1458,1460,1465,1470,1471,1474,1479,1481,1483,1485,1487,1491,1492,1497,1499,1500,1502,1506,1511,1516,1521,1526,1529,1534,1537,1541,1542,1545,1548,1552,1553,1554,1559,1562,1567,1569,1570,
        };

        String stolenTree = "{keys:[-1470,-450,269],children:[{keys:[-2001,-1900,-1741,-1616],children:[{keys:[-2153,-2133,-2108,-2079,-2060,-2045,-2016],children:[{keys:[-2173,-2169,-2166,-2163,-2161,-2159,-2158]},{keys:[-2150,-2145,-2140,-2135]},{keys:[-2130,-2127,-2123,-2119,-2117,-2116,-2115,-2112]},{keys:[-2107,-2103,-2099,-2094,-2089,-2085,-2080]},{keys:[-2074,-2073,-2069,-2067,-2066,-2065]},{keys:[-2057,-2054,-2052,-2050]},{keys:[-2040,-2037,-2035,-2033,-2028,-2026,-2022,-2019]},{keys:[-2015,-2014,-2012,-2008,-2003,-2002]}]},{keys:[-1987,-1960,-1942,-1926],children:[{keys:[-1996,-1994,-1992,-1991,-1989]},{keys:[-1983,-1981,-1977,-1972,-1967,-1966,-1965]},{keys:[-1956,-1953,-1952,-1949,-1948,-1943]},{keys:[-1939,-1936,-1934,-1931]},{keys:[-1921,-1920,-1918,-1914,-1913,-1911,-1906,-1902]}]},{keys:[-1883,-1869,-1850,-1828,-1813,-1793,-1782,-1765],children:[{keys:[-1898,-1894,-1890,-1888]},{keys:[-1880,-1879,-1878,-1873,-1871]},{keys:[-1864,-1861,-1859,-1856,-1854,-1853]},{keys:[-1848,-1843,-1839,-1837,-1836,-1833]},{keys:[-1826,-1824,-1823,-1822,-1821,-1818]},{keys:[-1810,-1808,-1806,-1802,-1798,-1796]},{keys:[-1792,-1789,-1786,-1785]},{keys:[-1780,-1778,-1777,-1776,-1772,-1769]},{keys:[-1763,-1761,-1760,-1757,-1752,-1747,-1742]}]},{keys:[-1720,-1709,-1693,-1671,-1645,-1630],children:[{keys:[-1740,-1737,-1732,-1727,-1726,-1725,-1723]},{keys:[-1719,-1715,-1711,-1710]},{keys:[-1705,-1702,-1698,-1695]},{keys:[-1692,-1689,-1684,-1683,-1679,-1676]},{keys:[-1669,-1666,-1662,-1657,-1654,-1649]},{keys:[-1642,-1637,-1636,-1633]},{keys:[-1627,-1625,-1621,-1620,-1619]}]},{keys:[-1599,-1573,-1550,-1533,-1519,-1485],children:[{keys:[-1612,-1610,-1607,-1603]},{keys:[-1595,-1591,-1590,-1586,-1584,-1581,-1579,-1578]},{keys:[-1570,-1566,-1565,-1562,-1560,-1555]},{keys:[-1545,-1542,-1539,-1538,-1536]},{keys:[-1529,-1527,-1522,-1521]},{keys:[-1514,-1510,-1506,-1503,-1499,-1494,-1490,-1487]},{keys:[-1484,-1483,-1478,-1474]}]}]},{keys:[-1359,-1254,-1068,-902,-783,-624],children:[{keys:[-1453,-1437,-1427,-1401,-1375],children:[{keys:[-1469,-1468,-1464,-1461,-1460,-1458]},{keys:[-1452,-1449,-1445,-1443,-1441,-1440]},{keys:[-1435,-1434,-1432,-1429]},{keys:[-1426,-1424,-1420,-1416,-1412,-1411,-1407,-1404]},{keys:[-1399,-1395,-1391,-1388,-1387,-1383,-1380,-1379]},{keys:[-1374,-1373,-1371,-1369,-1368,-1363,-1360]}]},{keys:[-1337,-1317,-1302,-1276],children:[{keys:[-1356,-1355,-1352,-1347,-1345,-1344,-1341,-1340]},{keys:[-1332,-1327,-1324,-1323,-1318]},{keys:[-1316,-1314,-1312,-1308,-1303]},{keys:[-1297,-1293,-1290,-1287,-1282,-1280]},{keys:[-1275,-1272,-1270,-1266,-1261,-1256]}]},{keys:[-1232,-1211,-1187,-1172,-1150,-1133,-1105,-1086],children:[{keys:[-1253,-1252,-1249,-1246,-1241,-1237]},{keys:[-1228,-1225,-1223,-1220,-1218,-1217,-1214]},{keys:[-1207,-1202,-1201,-1200,-1198,-1194,-1193,-1188]},{keys:[-1185,-1184,-1180,-1177]},{keys:[-1170,-1165,-1163,-1159,-1155]},{keys:[-1146,-1145,-1144,-1139,-1138]},{keys:[-1130,-1127,-1122,-1120,-1116,-1114,-1109]},{keys:[-1100,-1096,-1091,-1090]},{keys:[-1085,-1080,-1075,-1072]}]},{keys:[-1049,-1032,-1005,-989,-963,-940,-919],children:[{keys:[-1064,-1063,-1058,-1056,-1051]},{keys:[-1046,-1044,-1042,-1037]},{keys:[-1029,-1025,-1023,-1018,-1013,-1012,-1009,-1008]},{keys:[-1002,-998,-994,-990]},{keys:[-986,-985,-982,-978,-976,-971,-966,-965]},{keys:[-959,-957,-953,-950,-947,-945]},{keys:[-937,-936,-935,-931,-927,-924]},{keys:[-915,-913,-910,-907]}]},{keys:[-882,-866,-850,-828,-799],children:[{keys:[-898,-896,-892,-888,-886,-884]},{keys:[-881,-877,-876,-872,-870,-869]},{keys:[-862,-861,-858,-856,-855,-854]},{keys:[-848,-846,-841,-839,-836,-832,-831,-829]},{keys:[-827,-824,-822,-819,-814,-809,-805,-801]},{keys:[-795,-790,-786,-785]}]},{keys:[-765,-737,-717,-701,-673,-658,-643],children:[{keys:[-782,-778,-777,-775,-772,-771,-768]},{keys:[-763,-758,-756,-751,-748,-747,-744,-741]},{keys:[-733,-731,-729,-728,-725,-721]},{keys:[-713,-712,-711,-710,-706]},{keys:[-696,-693,-690,-686,-685,-682,-679,-675]},{keys:[-670,-665,-660,-659]},{keys:[-654,-650,-646,-645,-644]},{keys:[-639,-637,-633,-628]}]},{keys:[-597,-566,-538,-519,-505,-490,-479,-468],children:[{keys:[-622,-618,-616,-614,-609,-604,-603,-598]},{keys:[-594,-590,-586,-582,-577,-572,-569]},{keys:[-563,-558,-553,-548,-545,-542,-539]},{keys:[-537,-534,-529,-528,-527,-524,-522]},{keys:[-515,-514,-513,-510]},{keys:[-500,-497,-495,-493]},{keys:[-489,-484,-483,-482]},{keys:[-478,-474,-472,-469]},{keys:[-465,-462,-460,-458,-454]}]}]},{keys:[-331,-158,-64,123],children:[{keys:[-427,-400,-379,-360],children:[{keys:[-446,-442,-439,-438,-434,-431]},{keys:[-423,-422,-418,-414,-410,-406,-405,-404]},{keys:[-398,-397,-396,-395,-390,-389,-388,-383]},{keys:[-375,-372,-367,-365]},{keys:[-357,-353,-349,-345,-344,-341,-336]}]},{keys:[-308,-283,-263,-237,-224,-207,-193,-173],children:[{keys:[-330,-329,-326,-321,-318,-316,-314,-309]},{keys:[-303,-301,-297,-294,-289,-286]},{keys:[-281,-279,-277,-274,-271,-270,-269,-266]},{keys:[-260,-255,-254,-252,-250,-246,-241]},{keys:[-234,-233,-231,-228,-225]},{keys:[-221,-216,-212,-211]},{keys:[-205,-200,-198,-196,-195,-194]},{keys:[-191,-190,-188,-184,-179,-176]},{keys:[-168,-164,-163,-160]}]},{keys:[-139,-117,-98,-80],children:[{keys:[-153,-148,-143,-142]},{keys:[-135,-132,-128,-127,-126,-121]},{keys:[-114,-112,-107,-106,-104,-100]},{keys:[-97,-94,-92,-89,-84]},{keys:[-76,-74,-69,-67]}]},{keys:[-48,-11,2,25,58,75,105],children:[{keys:[-60,-59,-57,-52]},{keys:[-43,-39,-35,-30,-28,-23,-19,-15]},{keys:[-10,-9,-8,-7,-3]},{keys:[6,7,9,11,16,17,22]},{keys:[30,34,39,42,46,50,55,56]},{keys:[63,66,70,74]},{keys:[80,84,86,89,93,98,103,104]},{keys:[106,108,110,113,117,118]}]},{keys:[149,169,188,216,236,252],children:[{keys:[128,129,131,136,138,142,144]},{keys:[150,155,159,164,167]},{keys:[173,175,180,183,185]},{keys:[191,196,197,202,205,209,210,212]},{keys:[220,222,223,226,231,234]},{keys:[238,243,245,250]},{keys:[255,257,262,265]}]}]},{keys:[477,570,707,877,1001,1195,1343],children:[{keys:[293,332,347,373,386,408,433,459],children:[{keys:[271,272,277,282,287,291]},{keys:[297,300,304,309,314,318,323,327]},{keys:[337,340,343,345]},{keys:[350,355,358,363,368]},{keys:[375,379,381,382]},{keys:[389,391,393,397,399,403,407]},{keys:[411,415,417,421,426,429,430,432]},{keys:[436,438,443,446,449,454]},{keys:[464,468,469,470,473]}]},{keys:[491,507,520,537,556],children:[{keys:[479,481,482,483,486,487]},{keys:[495,496,497,499,504]},{keys:[509,512,515,518,519]},{keys:[523,525,526,529,530,531,534]},{keys:[541,543,547,548,551,554]},{keys:[557,560,562,564,569]}]},{keys:[590,618,648,667,688],children:[{keys:[574,578,581,583,585,589]},{keys:[594,598,602,606,611,613,617]},{keys:[619,621,625,630,632,637,642,643]},{keys:[649,653,655,657,659,662]},{keys:[671,676,677,678,681,684]},{keys:[693,698,699,704]}]},{keys:[732,758,776,797,811,823,841,858],children:[{keys:[711,716,720,722,724,728,729,730]},{keys:[735,740,745,746,747,749,752,756]},{keys:[763,767,770,771,773,774]},{keys:[778,781,786,790,793]},{keys:[798,801,803,808,810]},{keys:[813,816,819,821]},{keys:[824,827,832,833,838,840]},{keys:[846,847,851,853]},{keys:[859,860,861,863,868,871,874]}]},{keys:[895,912,931,952,979],children:[{keys:[880,881,885,886,888,890,893]},{keys:[900,905,906,907]},{keys:[917,921,925,928]},{keys:[934,936,938,939,940,942,943,947]},{keys:[955,960,965,970,973,974]},{keys:[983,984,988,991,995,996,999]}]},{keys:[1031,1049,1075,1098,1121,1143,1167],children:[{keys:[1006,1010,1014,1016,1018,1022,1023,1027]},{keys:[1032,1034,1035,1036,1040,1044]},{keys:[1051,1053,1057,1061,1064,1069,1071,1073]},{keys:[1076,1080,1083,1084,1085,1089,1092,1095]},{keys:[1103,1106,1108,1113,1116]},{keys:[1125,1129,1131,1135,1139]},{keys:[1145,1149,1154,1158,1159,1164]},{keys:[1171,1174,1175,1179,1180,1181,1185,1190]}]},{keys:[1223,1235,1248,1269,1294,1312,1325],children:[{keys:[1197,1201,1204,1208,1211,1215,1220,1221]},{keys:[1224,1225,1229,1230,1233]},{keys:[1239,1243,1246,1247]},{keys:[1253,1256,1258,1263,1264,1265,1267]},{keys:[1273,1275,1280,1285,1288,1292]},{keys:[1296,1298,1303,1308,1310]},{keys:[1314,1317,1319,1320,1322]},{keys:[1326,1329,1333,1335,1340]}]},{keys:[1375,1401,1428,1448,1471,1492,1521,1548],children:[{keys:[1346,1347,1350,1355,1357,1361,1366,1371]},{keys:[1377,1381,1382,1384,1385,1388,1393,1396]},{keys:[1404,1405,1408,1411,1414,1418,1421,1424]},{keys:[1432,1435,1438,1441,1443,1447]},{keys:[1453,1455,1458,1460,1465,1470]},{keys:[1474,1479,1481,1483,1485,1487,1491]},{keys:[1497,1499,1500,1502,1506,1511,1516]},{keys:[1526,1529,1534,1537,1541,1542,1545]},{keys:[1552,1553,1554,1559,1562,1567,1569,1570]}]}]}]}";

        for(int i = 0; i < stolenInts.length; i++){
            bTree0.insert(stolenInts[i]);
        }
        String bTreeZero = bTree0.toJson();

        Assert.assertEquals("unequal", stolenTree, bTreeZero);

        ---------------------------------------------------------------------------------------------------*/

        /*

        //bTree0.insert(-100);


        bTree0.hasKey(23);

        String json = bTree0.toJson();

        AbstractBTreeNode btreeNode = new BTreeNode(2);
        bTree0.setRoot(btreeNode);

        bTree0.insert(15);

        //BTreeNode.traverse(btreeNode);

        String test = bTree0.toJson();
        System.out.println(test);
        //String actual = "{keys:[9,22],children:[keys:{[2,8]},{keys:[17,21]},{keys:[23,24,25]}]}";
        Assert.assertEquals("unequal",actual, test);
/*
        int wcount = 0;
        while(bTree0.hasKey(15) && wcount < 1001){
            rnd = rand.nextInt(500);
            randomInts.add(rnd);
            bTree0.insert(rnd);
            System.out.println("last insertion: " + rnd);
            wcount++;
        }

        System.out.println("wcount: " + wcount);
*/
        int debug = -1;

    }
}
