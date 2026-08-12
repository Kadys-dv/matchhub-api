package dev.kadys.matchhub.config;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.UUID;

@Component
public class RequestCorrelationFilter extends OncePerRequestFilter {
    public static final String HEADER="X-Request-ID";
    @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {
        String requestId=request.getHeader(HEADER);
        if(requestId==null || requestId.isBlank() || requestId.length()>100) requestId=UUID.randomUUID().toString();
        MDC.put("requestId",requestId); response.setHeader(HEADER,requestId);
        try{chain.doFilter(request,response);}finally{MDC.remove("requestId");}
    }
}
