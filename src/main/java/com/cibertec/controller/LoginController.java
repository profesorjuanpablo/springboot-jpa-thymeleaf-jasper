package com.cibertec.controller;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.dto.RangoEdadDTO;
import com.cibertec.model.Tipo;
import com.cibertec.model.Usuario;
import com.cibertec.service.TipoService;
import com.cibertec.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class LoginController {

	private final UsuarioService usuarioService;
	private final TipoService tipoService;
	
	public LoginController(UsuarioService usuarioService, TipoService tipoService) {
        this.usuarioService = usuarioService;
        this.tipoService = tipoService;
    }
	
	@GetMapping("/")
	public String inicio() {
		return "login";
	}
	
	@GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
	
	@PostMapping("/login")
	public String login(@RequestParam String username,
	                    @RequestParam String password,
	                    Model model) {
	    try {
	        Usuario user = usuarioService.login(username, password);
	        model.addAttribute("nombres", user.getNomUsua());
	        model.addAttribute("apellidos", user.getApeUsua());
	        
	        if(user != null && user.getTipo().getIdTipo() == 1) {//1:administrativo
	            return "menu";
	        } else {
	            return "bienvenido"; 
	        }
	        
	        
	    } catch (RuntimeException e) {
	        model.addAttribute("mensajeError", e.getMessage());
	        return "login";
	    }
	}
	
	// Mostrar formulario
	@GetMapping("/registrar")
	public String nuevoUsuario(Model model){

	    Usuario usuario = new Usuario();
	    usuario.setTipo(new Tipo()); // 🔥 IMPORTANTE

	    model.addAttribute("usuario", usuario);
	    model.addAttribute("tipos", tipoService.listar());

	    return "registrar";
	}


    // Guardar
    @PostMapping("/guardar")
    public String guardarUsuario(Usuario usuario, Model model){      
        
        try {
        	usuarioService.guardar(usuario);
            model.addAttribute("mensajeOk", "Usuario registrado correctamente");
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al registrar el usuario");
        }
        return "registrar";
    }
    
    @GetMapping("/menu")
    public String mostrarMenu(Model model) {
        // Aquí podrías validar que hay una sesión activa
        return "menu";
    }
    
    //ESTO
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "listar";
    }
    //ESTO
    
    //METODO PARA EDITAR 
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Integer id, Model model) {

        Usuario usuario = usuarioService.buscarPorId(id);

        model.addAttribute("usuario", usuario);
        model.addAttribute("tipos", tipoService.listar());

        return "registrar";
    }
    
    
    @GetMapping("/usuarios/pdf")
    public void exportPDF(HttpServletResponse response) throws Exception{
    	//Tipo de archivo
    	response.setContentType("application/pdf");
    	response.setHeader("Content-Disposition","inline;filename=usuarios.pdf");
    	
    	//Obtener la lista desde JPA de Spring
    	List<Usuario> lista = usuarioService.listar();
    	
    	//Cargar JRXML desde Resources
    	InputStream inputStream = getClass().getResourceAsStream("/reportes/usuarios.jrxml");
    	
    	//Compilar el .JRXML a .JASPER
    	JasperReport jasperreport = JasperCompileManager.compileReport(inputStream);
    	
    	//Convertir la lista a Datasource
    	JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
    	
    	//Llenar pdf
    	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperreport,null, dataSource);
    	
    	//Exportar pdf
    	JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    	
    }
    
    @GetMapping("/usuarios/pdf-grafico")
    public void exportPDFGrafico(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline;filename=grafico_edades.pdf");

        List<RangoEdadDTO> rangoEdades = usuarioService.obtenerRangoEdades();

        // ✅ Agregar esto para verificar en consola
        System.out.println("Total rangos: " + rangoEdades.size());
        for (RangoEdadDTO r : rangoEdades) {
            System.out.println(r.getRango() + " -> " + r.getCantidad());
        }
        
        InputStream inputStream = getClass().getResourceAsStream("/reportes/graficoEdades.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

      //Convertir la lista a Datasource
    	JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rangoEdades);
    	

        // ✅ Pasar la lista como parámetro
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("dsGrafico", dataSource);

        // Datasource vacío para el reporte principal
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    
}