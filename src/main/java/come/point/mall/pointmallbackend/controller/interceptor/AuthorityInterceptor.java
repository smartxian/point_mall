package come.point.mall.pointmallbackend.controller.interceptor;

import com.google.common.collect.Maps;
import come.point.mall.pointmallbackend.common.Const;
import come.point.mall.pointmallbackend.common.ServerResponse;
import come.point.mall.pointmallbackend.pojo.User;
import come.point.mall.pointmallbackend.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handle) throws Exception {
        log.info("preHandle");
        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handle;
        //解析handlerMethod
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体的参数key以及value是什么，方便打印日志
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = (String) entry.getKey();
//            String mapValue = (String) entry.getValue();
            String mapValue = StringUtils.EMPTY;
            //httpServletRequest的这个map，里面的value返回的是一个String[]
            Object obj = entry.getValue();

            if (obj instanceof String[]) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        if (StringUtils.equals(className,"UserManageController") && StringUtils.equals(methodName,"login")) {
            log.info("拦截器拦截到请求，className:{},method:{}",className,methodName);
            return true;
        }
        log.info("拦截器拦截到请求，className:{},method:{},params:{}",className,methodName,requestParamBuffer.toString());

        User user = null;
        HttpSession session = httpServletRequest.getSession();
        user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null || user.getRole().intValue() != Const.Role.ROLE_ADMIN) {
            //返回false，即不会调用Controller中的方法
            httpServletResponse.reset();//这里要添加reset，否则会报异常
            httpServletResponse.setCharacterEncoding("UTF-8");//这里要设置编码，否则会乱码
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter out = httpServletResponse.getWriter();
            if (user == null) {
                //富文本文件上传特殊处理
                if (StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtestImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请先登录管理员");
                    out.print(JsonUtil.Object2String(resultMap));
                } else {
                    out.print(JsonUtil.Object2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            } else {
                if (StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richtestImgUpload")) {
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权限操作");
//                    out.print(JsonUtil.Object2String(resultMap));
                } else {
//                    out.print(JsonUtil.Object2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }
            out.flush();
            out.close();  //这里要关闭
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
