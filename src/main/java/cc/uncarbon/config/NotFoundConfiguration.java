package cc.uncarbon.config;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 由于除 404 以外的异常都会被全局异常处理掉，所以走到这里的请求都是 404 了
 *
 * @author Uncarbon
 */
@RestController
public class NotFoundConfiguration implements ErrorController {

    /**
     * 不生成接口文档
     * 主动抛出异常，由全局异常处理接管
     */
    @Operation(hidden = true)
    @RequestMapping(value = "/error")
    public void error(HttpServletRequest request) throws NoHandlerFoundException {
        throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI(), new HttpHeaders());
    }
}
