package com.controller;

import com.github.pagehelper.PageHelper;
import com.mapper.TestMapper;
import com.model.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private TestMapper testMapper;

    /*
     * ①利用类似池化技术每天只初始化一次propertyMap
     * ②Controller在Action外只能声明变量
     * */
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
    private static String date = format.format(new Date());
    private static Map propertyMap = new HashMap(); /* 包含属性名+不同属性的列表 */

    /************************************************************分割线************************************************************/
    /* todo 搜索 */

    /* 搜索条件收集 */
    @ResponseBody
    @RequestMapping("/property")
    public Map property() {

        String now = format.format(new Date());
        if (!date.equals(now) || propertyMap.size() == 0) {
            String[] propertyArray = {"property2", "property3"}; /* 实体类需要group by的属性 */
            for (int i = 0; i < propertyArray.length; i++) {
                List propertyList = new ArrayList();
                List<Test> testList = testMapper.selectPropertyGroup(propertyArray[i]);
                for (int j = 0; j < testList.size(); j++) {
                    propertyList.add(testList.get(j).getCommonProperty());
                }
                propertyMap.put(propertyArray[i], propertyList);
            }
            date = now; /* 更新全局时间 */
        }

        Map result = new HashMap();
        return result;
    }

    /************************************************************分割线************************************************************/

    /* 多重模糊搜索（AND） */
    @ResponseBody
    @RequestMapping("/fuzzy")
    public Map fuzzy() {

        Map select = new HashMap();
        List propertyList = new ArrayList();
        propertyList.add("a");
        propertyList.add("b");
        select.put("property1", propertyList);
        List<Test> testList = testMapper.selectFuzzy(select);

        Map result = new HashMap();
        return result;
    }

    /* 多重模糊+条件搜索（property1作为name模糊搜索，p2/p3为条件） */
    @ResponseBody
    @RequestMapping("/fuzzy2")
    public Map fuzzy2(@RequestParam(defaultValue = "1") Integer currentPage) {

        Map select = new HashMap();
        List propertyList = new ArrayList();
        propertyList.add("a");
        propertyList.add("b");
        select.put("property1", propertyList);
        propertyList = new ArrayList();
        propertyList.add("b1");
        propertyList.add("b4");
        select.put("property2", propertyList);
        propertyList = new ArrayList();
        propertyList.add("c5");
        propertyList.add("c8");
        select.put("property3", propertyList);

        PageHelper.startPage(currentPage, 10); /* PageHelper分页插件 */
        List<Test> testList = testMapper.selectFuzzy2(select);

        Map result = new HashMap();
        return result;
    }

}
