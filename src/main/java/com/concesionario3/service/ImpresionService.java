package com.concesionario3.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

import com.concesionario3.domain.User;
import com.concesionario3.domain.Venta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import org.thymeleaf.spring5.SpringTemplateEngine;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.io.IOException;

@Service
public class ImpresionService {

    private final Logger log = LoggerFactory.getLogger(ImpresionService.class);;
    private final JHipsterProperties jHipsterProperties;

    private Venta factura;
    private final UserService userService;

    private final SpringTemplateEngine templateEngine;

    public ImpresionService(JHipsterProperties jHipsterProperties, SpringTemplateEngine templateEngine,
            UserService userService) {
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
        this.userService = userService;

    }

    public ResponseEntity<byte[]> printVenta(List<Venta> venta) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Content-Disposition", "attachment;filename-invoice.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        log.debug("Generando invoice...");
        User user = userService.getUserWithAuthorities().get();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);

        context.setVariable("ventas", venta);

        String templateContent = "";

        templateContent = templateEngine.process("informes/ventasTerminadas.html", context);

        ByteArrayOutputStream pdfContents = new ByteArrayOutputStream();

        // process html
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(templateContent, "/");
            builder.toStream(pdfContents);
            builder.run();
            pdfContents.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return new ResponseEntity<byte[]>(pdfContents.toByteArray(), headers, HttpStatus.OK);

    }

    public ResponseEntity<byte[]> printFactura(Venta venta) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Content-Disposition", "attachment;filename-invoice.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        log.debug("Generando invoice...");
        User user = userService.getUserWithAuthorities().get();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);

        context.setVariable("venta", venta);

        String templateContent = "";

        templateContent = templateEngine.process("informes/facturas.html", context);

        ByteArrayOutputStream pdfContents = new ByteArrayOutputStream();

        // process html
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(templateContent, "/");
            builder.toStream(pdfContents);
            builder.run();
            pdfContents.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return new ResponseEntity<byte[]>(pdfContents.toByteArray(), headers, HttpStatus.OK);

    }

    public ResponseEntity<byte[]> printVentaVendedor(List<Venta> venta) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Content-Disposition", "attachment;filename-invoice.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        log.debug("Generando invoice...");
        User user = userService.getUserWithAuthorities().get();
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);

        context.setVariable("ventas", venta);

        String templateContent = "";

        templateContent = templateEngine.process("informes/ventasTerminadasVendedor.html", context);

        ByteArrayOutputStream pdfContents = new ByteArrayOutputStream();

        // process html
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(templateContent, "/");
            builder.toStream(pdfContents);
            builder.run();
            pdfContents.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }

        return new ResponseEntity<byte[]>(pdfContents.toByteArray(), headers, HttpStatus.OK);

    }

}
