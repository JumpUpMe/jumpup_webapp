/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.rest.controller;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.rest.methods.IDelete;
import de.htw.fb4.imi.jumpup.rest.methods.IGet;
import de.htw.fb4.imi.jumpup.rest.methods.IOptions;
import de.htw.fb4.imi.jumpup.rest.methods.IPost;
import de.htw.fb4.imi.jumpup.rest.methods.IPut;
import de.htw.fb4.imi.jumpup.rest.response.builder.IErrorResponseEntityBuilder;
import de.htw.fb4.imi.jumpup.rest.response.builder.IResponseEntityBuilder;
import de.htw.fb4.imi.jumpup.rest.response.builder.ISuccessResponseEntityBuilder;
import de.htw.fb4.imi.jumpup.rest.response.model.AbstractRestModel;
import de.htw.fb4.imi.jumpup.translate.Translatable;
import de.htw.fb4.imi.jumpup.util.ErrorPrintable;
import de.htw.fb4.imi.jumpup.validation.ValidationException;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 25.11.2015
 *
 */
public abstract class AbstractRestController<T extends AbstractRestModel>
        implements IGet, IPost<T>, IPut<T>, IDelete, IOptions
{
    protected static final String QUERY_PARAM_LIMIT = "limit";
    protected static final String QUERY_PARAM_OFFSET = "offset";

    @Inject
    protected IResponseEntityBuilder responseEntityBuilder;

    @Inject
    protected Translatable translator;

    protected boolean isEnabled()
    {
        return false;
    }

    @Override
    @GET
    public Response get(@Context HttpHeaders headers)
    {
        if (!this.isEnabled()) {
            return this.sendVersionDisabledResponse();
        }

        return null;
    }

    @Override
    @GET
    public Response get(@Context HttpHeaders headers, Long... ids)
    {
        if (!this.isEnabled()) {
            return this.sendVersionDisabledResponse();
        }

        return null;
    }

    @Override
    @POST
    public Response post(@Context HttpHeaders headers, T abstractRestModel)
    {
        if (!this.isEnabled()) {
            return this.sendVersionDisabledResponse();
        }

        return null;
    }

    @Override
    @PUT
    public Response put(@Context HttpHeaders headers, Long entityId,
            T abstractRestModel)
    {
        if (!this.isEnabled()) {
            return this.sendVersionDisabledResponse();
        }

        return null;
    }

    @Override
    @DELETE
    public Response delete(@Context HttpHeaders headers, Long entityId)
    {
        if (!this.isEnabled()) {
            return this.sendVersionDisabledResponse();
        }

        return null;
    }

    @Override
    @OPTIONS
    public Response options(@Context HttpHeaders headers)
    {
        if (!this.isEnabled()) {
            return this.sendVersionDisabledResponse();
        }

        return null;
    }

    private Response sendVersionDisabledResponse()
    {
        return Response.notAcceptable(null)
                .entity(this.responseEntityBuilder.buildMessageFromErrorString(
                        IErrorResponseEntityBuilder.MESSAGE_VERSION_DISABLED))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendNotFoundResponse(String message)
    {
        return Response.status(Status.NOT_FOUND)
                .entity(this.responseEntityBuilder
                        .buildMessageFromErrorString(message))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendInternalServerErrorResponse(
            ErrorPrintable errorPrintable)
    {
        return sendInternalErrorResponse(errorPrintable.getSingleErrorString());
    }

    protected Response sendInternalErrorResponse(String msg)
    {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity(this.responseEntityBuilder
                        .buildMessageFromErrorString(msg))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendOkResponse(String message)
    {
        return Response
                .ok(this.responseEntityBuilder.buildSuccessFromString(message))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendPutOkResponse(String entityType, long entityId)
    {
        return Response
                .ok(this.responseEntityBuilder.buildSuccessFromString(String
                        .format(ISuccessResponseEntityBuilder.MESSAGE_UPDATED,
                                entityType, entityId)))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendOkButErrorResponse(String errorMessage)
    {
        return Response.ok()
                .entity(this.responseEntityBuilder
                        .buildMessageFromErrorString(errorMessage, true))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendOkButErrorResponse(ApplicationUserException e)
    {
        return this.sendOkButErrorResponse(e.getUserMsg());
    }

    protected Response sendCreatedResponse(String entityType, long entityId)
    {
        return Response.created(null)
                .entity(this.responseEntityBuilder.buildSuccessFromString(String
                        .format(ISuccessResponseEntityBuilder.MESSAGE_CREATED,
                                entityType, entityId)))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendCreatedButErrorResponse(String errorMessage)
    {
        return Response.created(null)
                .entity(this.responseEntityBuilder
                        .buildMessageFromErrorString(errorMessage, true))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendInternalServerErrorResponse(String errorMessage)
    {
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity(this.responseEntityBuilder
                        .buildMessageFromErrorString(errorMessage))
                .type(MediaType.APPLICATION_JSON).build();
    }

    protected Response sendInternalServerErrorResponse(
            ApplicationUserException tripCreationException)
    {
        return this
                .sendInternalErrorResponse(tripCreationException.getUserMsg());
    }

    protected Response sendBadRequestResponse(ValidationException e)
    {
        return Response.status(Status.BAD_REQUEST)
                .entity(this.responseEntityBuilder
                        .buildMessageFromValidationException(e))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
