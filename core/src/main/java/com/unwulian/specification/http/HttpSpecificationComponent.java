package com.unwulian.specification.http;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Joiner;
import com.unwulian.specification.bean.TableBean;
import com.unwulian.specification.codemodel.MdModel;
import com.unwulian.specification.parser.IParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpSpecificationComponent {
    private HttpDictSpecification httpDictSpecification = new HttpDictSpecification();
    private HttpRequestSpecification httpRequestSpecification = new HttpRequestSpecification();
    private HttpResponseSpecification httpResponseSpecification = new HttpResponseSpecification();
    private String dictReq;
    private String dictResp;
    private String request;
    private String response;
    private String title;
    private String type;
    private String[] dictReqAppend;
    private String[] dictRespAppend;
    public HttpSpecificationComponent(String dictReq, String dictResp, String request, String response, String title, String type) {
        this.dictReq = dictReq;
        this.dictResp = dictResp;
        this.request = request;
        this.response = response;
        this.title = title;
        this.type = type;
    }

    public String[] getDictReqAppend() {
        return dictReqAppend;
    }

    public void setDictReqAppend(String...dictReqAppend) {
        this.dictReqAppend = dictReqAppend;
    }

    public String[] getDictRespAppend() {
        return dictRespAppend;
    }

    public void setDictRespAppend(String...dictRespAppend) {
        this.dictRespAppend = dictRespAppend;
    }

    private boolean check() {
        return httpDictSpecification.check(dictReq)
                && httpDictSpecification.check(dictResp)
                && httpRequestSpecification.check(request)
                && httpResponseSpecification.check(response);
    }

    public String parse() {
        if (!check()) {
            throw new RuntimeException("文件不规范，请检查文件内容");
        }

        List<TableBean> dictBeansReq = httpDictSpecification.parse(dictReq);
        appendTableBeans(dictBeansReq,dictReqAppend);

        List<TableBean> dictBeansResp = httpDictSpecification.parse(dictResp);
        appendTableBeans(dictBeansResp,dictRespAppend);
        String requestStr = httpRequestSpecification.parse(request);
        String responseStr = httpResponseSpecification.parse(response);
        String populateRequest = populateLine(dictBeansReq, requestStr);
        String populateResponse = populateLine(dictBeansResp, responseStr);
        String body = Joiner.on(System.lineSeparator()).join("request", populateRequest, "response", populateResponse);
        String mdContent = MdModel.ZD_MODEL.replace(MdModel.TITLE_PLACE_HOLDER, title)
                .replace(MdModel.TYPE_PLACE_HOLDER, type)
                .replace(MdModel.BODY_PLACE_HOLDER, body);
        return mdContent;
    }

    private void appendTableBeans(List<TableBean> dictBeansReq,String[] append) {
        if(null != append){
            for (String s : append) {
                List<TableBean> parse = httpDictSpecification.parse(s);
                if(CollUtil.isNotEmpty(parse)){
                    dictBeansReq.addAll(parse);
                }
            }
        }
    }

    private String populateLine(List<TableBean> dictBeans, String requestStr) {
        List<String> newLines = new ArrayList<>();
        String[] lines = requestStr.split(IParser.LINE_SEPERATOR);
        for (String line : lines) {
            if (line.contains("#")) {
                newLines.add(line);
                continue;
            }
            String key = line.trim();
            try {
                TableBean tableBean = getTableBean(key, dictBeans);
                newLines.add(Joiner.on(":").join(line, tableBean.getType(), tableBean.getComment()));
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return newLines.stream().collect(Collectors.joining(System.lineSeparator()));
    }


    private TableBean getTableBean(String key, List<TableBean> dictBeans) {
        try {
            return dictBeans.stream().filter(tableBean -> tableBean.getName().equalsIgnoreCase(key.trim()))
                    .findFirst().get();
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        String dictReq = "[    [        \"名称\",        \"定义\",        \"必\\n需\",        \"长度/范围\",        \"类型\",        \"备注\"    ],    [        \"Name\",        \"固定 \\\"personListRequest\\\"\",        \"Y\",        \"\",        \"字符串\",        \"\"    ],    [        \"UUID\",        \"设备UUID\",        \"N\",        \"<32\",        \"字符串\",        \"\"    ],    [        \"ChannelNo\",        \"通道号 （NVR）\",        \"N\",        \"\",        \"整型\",        \"\"    ],    [        \"Session\",        \"注册返回Session\",        \"N\",        \"\",        \"字符串\",        \"\"    ],    [        \"TimeStamp\",        \"Unix时间戳(秒)\",        \"Y\",        \"固定 10\",        \"整型\",        \"\"    ],    [        \"Sign\",        \"规则生成的Sign值\",        \"N\",        \"固定32\",        \"字符串\",        \"\"    ],    [        \"Data\",        \"请求内容\",        \"Y\",        \"\",        \"Json对象\",        \"\"    ],    [        \"Action\",        \"固定 \\\"addPerson\\\"\",        \"Y\",        \"\",        \"字符串\",        \"\"    ],    [        \"AddType\",        \"添加方式\\n0：图片添加（默认）\\n1：特征值添加（同步）\\n2：特征值添加（异步）\\n（不支持）\\n3：IC卡添加（只识别 IC\\n卡不识别人脸）\",        \"N\",        \"\",        \"整型\",        \"特征值同步添\\n加直接返回结\\n果（时间长）；\\n异步添加通过\\n通知信息上报\\n结果。\\n异步方式请用批量添加人员\\n接口。\"    ],    [        \"PersonType\",        \"名单类型：\\n1：黑名单\\n2：白名单\\n3：VIP名单\",        \"Y\",        \"\",        \"整型\",        \"\"    ],    [        \"PersonInfo\",        \"添加的人员信息\\n（具体参数见3.2）\",        \"Y\",        \"\",        \"Json对象\",        \"\"    ],    [        \"PersonCover\",        \"是否覆盖添加\\n0：不覆盖 （已存在该人\\n员ID则不添加该人员，返\\n回错误信息）\\n1：覆盖（若存在该人员\\nID 则覆盖该已存在人员\\nID 信息；若不存人员 ID\\n则添加该人员。）\",        \"N\",        \"\",        \"整型\",        \"\"    ],    [        \"PersonId\",        \"人员ID\",        \"Y\",        \"<36\",        \"字符串\",        \"\"    ],    [        \"PersonName\",        \"人员 姓名\",        \"N\",        \"<64\",        \"字符串\",        \"\"    ],    [        \"PersonPhoto\",        \"人员照片(base64编码)\\n图片格式: JPG\\n若通过特征值的方式添\\n加人员，可以不带“人员\\n照片”字段。\",        \"N\",        \"<2M\\n< 960 *\\n960\",        \"字符串\",        \"\"    ],    [        \"FeatureValue\",        \"人员特征值数据\\n(base64编码)\\n若通过特征值的方式添\\n加人员，必须带“人员特\\n征值数据”字段。\",        \"N\",        \"\",        \"字符串\",        \"\"    ]]";
        String dictResp = "[        [            \"名称\",            \"定义\",            \"必需\",            \"长度/范围\",            \"类型\",            \"备注\"        ],        [            \"Name\",            \"固 定 \\\"\\npersonListResponse\\\"\",            \"Y\",            \"\",            \"字符串\",            \"\"        ],        [            \"Session\",            \"注册返回Session\",            \"N\",            \"\",            \"字符串\",            \"\"        ],        [            \"Data\",            \"返回内容\",            \"Y\",            \"\",            \"JSON对象\",            \"\"        ],        [            \"Action\",            \"固定 \\\"addPerson\\\"\",            \"Y\",            \"\",            \"字符串\",            \"\"        ],        [            \"PersonType\",            \"名单类型：\\n1：黑名单\\n2：白名单\\n3：VIP名单\",            \"Y\",            \"\",            \"整型\",            \"\"        ],        [            \"PersonInfo\",            \"获取的人员信息\\n（具体参数见3.2）\",            \"Y\",            \"\",            \"Json对象\",            \"\"        ],        [            \"Result\",            \"内部错误码\",            \"N\",            \"\",            \"整型\",            \"Code=1110时，添加\\n失败返回\\n的内部操\\n作 错 误\\n码。\"        ],        [            \"Code\",            \"返回操作码\\n1 成功\",            \"Y\",            \"\",            \"整型\",            \"\"        ],        [            \"Message\",            \"返回操作信息\",            \"Y\",            \"最大200\",            \"字符串\",            \"\"        ]    ]";

        String req = "{    \"Name\": \"personListRequest\",    \"UUID\": \"abcdefabcdef\",    \"Session\": \"abcdefabcdef_1555559039\",    \"TimeStamp\": 1542594185,    \"Sign\": \"ec304c35342cbe0609e7c7fec4d97ca5\",    \"Data\": {        \"Action\": \"addPerson\",        \"AddType\": 1,        \"PersonType\": 2,        \"PersonInfo\": {            \"PersonCover\": 1,            \"PersonId\": \"Person1\",            \"PersonName\": \"PersonName1\",            \"Sex\": 1,            \"IDCard\": \"IDCard\",            \"Nation\": \"Nation\",            \"Birthday\": \"Birthday\",            \"Phone\": \"Phone\",            \"Address\": \"Address\",            \"LimitTime\": 0,            \"StartTime\": \"2020-01-01 00:00:00\",            \"EndTime\": \"2021-01-01 00:00:00\",            \"PersonIdentity\": 1,            \"IdentityAttribute\": 1,            \"TimeTable\": [                0,                0,                0,                0,                0,                0,                0,                0,                0            ],            \"Label\": \"Lable\",            \"ICCardNo\": \"No_001\",            \"ICCardNoList\": [                \"No_002\",                \"No_003\"            ],                        \"PersonPhoto\": \"...(base64)...\",            \"FeatureValue\": \"...(base64)...\"        }    }}";
        String resp = "{    \"Name\": \"personListResponse\",    \"Session\": \"abcdefabcdef_1555559039\",    \"Data\": {        \"Action\": \" addPerson\",        \"PersonType\": 2,        \"PersonInfo\": {            \"PersonId\": \"Person1\",            \"PersonName\": \"PersonName1\",            \"Sex\": 1,            \"IDCard\": \"IDCard\",            \"Nation\": \"Nation\",            \"Birthday\": \"Birthday\",            \"Phone\": \"Phone\",            \"Address\": \"Address\",            \"LimitTime\": 1,            \"StartTime\": \"2019-01-01 00:00:00\",            \"EndTime\": \"2020-01-01 00:00:00\",            \"PersonIdentity\": 1,            \"IdentityAttribute\": 1,            \"Label\": \"Lable\",            \"ICCardNo\": \"No_001\",            \"ICCardNoList\": [                \"No_002\",                \"No_003\"            ]        },        \"Result\": -1    },    \"Code\": 0,    \"Message\": \" \"}";
        HttpSpecificationComponent httpSpecificationComponent = new HttpSpecificationComponent(dictReq, dictResp, req, resp,"设备信息","deviceInfo");
        String reqAppend = "[['名称', '定义', '必\\n需', '长度/范围', '类型', '备注'], ['PersonInfo', '人员信息', 'N', '', 'Json对象', ''], ['PersonId', '人员ID', 'Y', '<36', '字符串', ''], ['PersonName', '人员 姓名', 'N', '<64', '字符串', ''], ['Sex', '性别\\n1：男 2：女 0：未知', 'N', '0-2', '整型', ''], ['IDCard', '身份证编号', 'N', '<32', '字符串', ''], ['Nation', '民族', 'N', '<12', '字符串', ''], ['Birthday', '生日', 'N', '<24', '字符串', ''], ['Phone', '电话号码', 'N', '<16', '字符串', ''], ['Address', '住址', 'N', '<100', '字符串', ''], ['SaveTime', '人员添加时间', 'N', '<20', '字符串', ''], ['LimitTime', '人员有效时间限制\\n0: 永久有效\\n1: 周期有效', 'N', '0-1', '整型', ''], ['StartTime', '人员有效开始时间\\n格式:\\nyyyy-mm-ddhh:mm:ss', 'N', '<20', '字符串', ''], ['EndTime', '人员有效结束时间\\n格式:\\nyyyy-mm-ddhh:mm:ss', 'N', '<20', '字符串', ''], ['PersonIdentity', '人员身份：用于名单分类\\n0：无\\n1：老师\\n2：走读（学生）\\n3：寄读（学生）\\n4：访客\\n......', 'N', '0~4', '整型', '（仅有名单分组\\n功能的门禁机支\\n持）'], ['IdentityAttribute', '人员身份属性：用于名单\\n分组\\n（自定义分组信息）\\n0：无\\n1：领导/高三\\n2：助教/初三\\n......', 'N', '0~10', '整型', '（仅有名单分组\\n功能的门禁机支\\n持）'], ['TimeTable', '关联通行时间段\\n关联第x个时间段方案就\\n数组第x个值赋值1。\\n例如：\\n\"TimeTable\":[1,0,1,0,...,0]\\n代表关联时间段1和时间\\n段3', 'N', '20', 'Json数组\\n（整型）', '（仅有名单分组\\n功能的门禁机支\\n持）\\n时间段方案具\\n体时间段由“通\\n行时间段设置”\\n接口来设置。支\\n持20个时间段方案。'], ['Label', '人员标签', 'N', '<120', '字符串', ''], ['ICCardNo', '绑定的IC卡号', 'N', '<16', '字符串', ''], ['ICCardNoList', '扩展IC卡号列表', 'N', '2', 'Json数组\\n(字符串)', '列表仅支持扩\\n展 2 个，超出\\n无效。'], ['PersonExtension', '扩展人员信息（自定义保\\n存的内容）', 'N', '', 'JSON 对\\n象', ''], ['PersonCode1', '自定义编码1', 'N', '<32', '字符串', ''], ['PersonCode2', '自定义编码2', 'N', '<32', '字符串', ''], ['PersonCode3', '自定义编码3', 'N', '<32', '字符串', ''], ['PersonReserveNa\\nme', '自定义备用名称', 'N', '<32', '字符串', ''], ['PersonParam1', '自定义参数1', 'N', '', '整型', ''], ['PersonParam2', '自定义参数2', 'N', '', '整型', ''], ['PersonParam3', '自定义参数3', 'N', '', '整型', ''], ['PersonParam4', '自定义参数4', 'N', '', '整型', ''], ['PersonParam5', '自定义参数5', 'N', '', '整型', ''], ['PersonData1', '自定义信息1', 'N', '<32', '字符串', ''], ['PersonData2', '自定义信息2', 'N', '<24', '字符串', ''], ['PersonData3', '自定义信息3', 'N', '<32', '字符串', ''], ['PersonData4', '自定义信息4', 'N', '<36', '字符串', ''], ['PersonData5', '自定义信息5', 'N', '<88', '字符串', ''], ['PersonPhoto', '人员照片(base64编码)', 'N', '<2M\\n< 960 *\\n960', '字符串', ''], ['FeatureValue', '人员特征值数据(base64\\n编码)', 'N', '', '字符串', '若通过特征值\\n的方式添加人\\n员，必须带“人\\n员特征值数据”\\n字段。']]";
        String respAppend = reqAppend;
        httpSpecificationComponent.setDictReqAppend(reqAppend);
        httpSpecificationComponent.setDictRespAppend(respAppend);
        String parse = httpSpecificationComponent.parse();
        System.out.println(parse);
    }
}
