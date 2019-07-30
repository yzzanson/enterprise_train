package com.enterprise;

//import org.junit.Test;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/17 上午9:59
 */
public class CommonTest {

   // @Test
    public void getAllPetName(){
        String petNames = "长腿萌鸡;柠檬不萌;长颈猫;草莓欧尼;喵咩咩;土豆丝;炒年糕;红烧肉;太妃糖;猪猪侠;蹦擦擦;泡泡糖;果味喵;宝宝比人帅;大力水脚;软甜暴力妹;冬菇头;小虎牙;我怎么这么强;东海小霸王;哈雷霸锤;有种放学别跑;重生的撒旦;金牌打手;小火柴;森林牧歌;萝莉味软妹酥;蝉鸣半夏;菜鸟不吃菜;汪了个汪;超人不会飞;不明爬行物;果冻不丁;全民萌宠;比巴卜;章鱼小丸子;动感光波;咸蛋超人;酱果君;旺仔小馒头;超神战士;虚空行者;哆啦B梦;小怪兽;酸菜鱼;清风与酒;月下独酌;天涯为客;白衣酒客;小丸子";
        String[] petNameArray = petNames.split(";");
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < petNameArray.length; i++) {
            if(sbf.length()==0){
                sbf.append("'").append(petNameArray[i]).append("'");
            }else{
                sbf.append(",").append("'").append(petNameArray[i]).append("'");
            }
        }
        System.out.println(sbf.toString());
    }

    public void getString(){
//        String str = "\uD83C\uDDE8\uD83C\uDDF3陈栖文   ♻️『近视防控-孝感™』";
//        String str2=  "猫\uD83D\uDC31";
    }

}
