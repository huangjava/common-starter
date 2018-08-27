package com.yihu.base.security.sms.mobile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenweida on 2017/12/8.
 */
public class DefaultMobileCheck implements MobileCheck {
    @Override
    public Boolean checkMobile(String mobile) {
        return checkCellphone(mobile);
    }

    /**
     * 验证手机号码
     * <p>
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189,181
     *
     * @param cellphone
     * @return
     */
    public boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,5-9]))\\d{8}$";
        return check(cellphone, regex);
    }

    /**
     * 使用正则表达式进行表单验证
     */
    public boolean check(String str, String regex) {
        boolean flag = false;
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
