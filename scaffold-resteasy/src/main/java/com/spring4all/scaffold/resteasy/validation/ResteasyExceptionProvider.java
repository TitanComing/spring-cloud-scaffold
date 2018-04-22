package com.spring4all.scaffold.resteasy.validation;

import com.spring4all.scaffold.common.AuthorizationException;
import com.spring4all.scaffold.common.BaseErrorCode;
import com.spring4all.scaffold.common.BaseResult;
import com.spring4all.scaffold.common.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author fangzhibin
 */
public class ResteasyExceptionProvider implements ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(ResteasyExceptionProvider.class);

    @Override
    public Response toResponse(Exception e) {
        if(e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            BaseResult baseResult = new BaseResult(BaseResult.FAIL_TYPE, businessException.getCode(), businessException.getMsg(), businessException.getData());
            return buildResponse(e, Status.BAD_REQUEST, baseResult);
        } else if (e instanceof AuthorizationException) {
            AuthorizationException be = (AuthorizationException) e;
            BaseResult baseResult = new BaseResult<>(BaseResult.NO_AUTH_TYPE,
                be.getCode(), be.getMsg());
            return buildResponse(e, Status.FORBIDDEN, baseResult);
        }  else {
            BaseResult<String> baseResult = new BaseResult<>(BaseResult.ERROR_TYPE, BaseErrorCode.SYSTEM_INTERNAL_ERROR.getCode(), BaseErrorCode.SYSTEM_INTERNAL_ERROR.getMsg());
            return buildResponse(e, Status.INTERNAL_SERVER_ERROR, baseResult);
        }
    }

    public static Response buildResponse(Exception exception, Status status, BaseResult baseResult) {
        logger.error("the interface caused error", exception);
        ResponseBuilder builder = Response.status(status);
        builder.type(MediaType.APPLICATION_JSON);
        builder.entity(baseResult);
        return builder.build();
    }

}
