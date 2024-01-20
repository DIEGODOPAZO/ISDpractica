package es.udc.ws.app.restservice.servlets;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.partido.Partido;
import es.udc.ws.app.model.partidoservice.PartidoServiceFactory;
import es.udc.ws.app.model.partidoservice.exceptions.AlreadyCollectedException;
import es.udc.ws.app.model.partidoservice.exceptions.InvalidCardException;
import es.udc.ws.app.restservice.dto.PartidoToRestPartidoDtoConversor;
import es.udc.ws.app.restservice.dto.RestPartidoDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestPartidoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PartidoServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestPartidoDto partidoDto = JsonToRestPartidoDtoConversor.toRestPartidoDto(req.getInputStream());
        Partido partido = PartidoToRestPartidoDtoConversor.toPartido(partidoDto);


        partido = PartidoServiceFactory.getService().addPartido(partido);

        partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), null);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        String dateS = req.getParameter("date");
        if (dateS != null) {
            ServletUtils.checkEmptyPath(req);
            // Formato de la cadena
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            // Convertir la cadena a LocalDateTime
            LocalDateTime dateL = LocalDateTime.parse(dateS, formatter);


            List<Partido> partidos = PartidoServiceFactory.getService().findPartidoByDates(LocalDateTime.now(), dateL);

            List<RestPartidoDto> partidoDtos = PartidoToRestPartidoDtoConversor.toRestPartidoDtos(partidos);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestPartidoDtoConversor.toArrayNode(partidoDtos), null);
        } else {
            Long id = ServletUtils.getIdFromPath(req, "partidoId");
            Partido partido = PartidoServiceFactory.getService().findPartidoById(id);
            RestPartidoDto partidoDto = PartidoToRestPartidoDtoConversor.toRestPartidoDto(partido);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestPartidoDtoConversor.toObjectNode(partidoDto), null);
        }
    }

}

