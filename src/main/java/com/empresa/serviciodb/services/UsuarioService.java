package com.empresa.serviciodb.services;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${db.catalogo}")
	private String db_catalogo;
	
	/* 
	 * es una clase de Spring que simplifica el uso de JDBC y facilita la ejecución 
	 * de consultas SQL y llamadas a procedimientos almacenados.
	 * se utiliza para interactuar con la base de datos sin la necesidad de 
	 * escribir manualmente el código para manejar conexiones, declaraciones, 
	 * ejecución de consultas, etc.
	 */
	
	
	//quiero obtener el nombre y apellido de un cliente del banco
	public String obtenerNombreCompletoCliente(int idCliente) throws SQLException{
		
		try	{
			/*
			 * 	Dentro del método, se crea un objeto SimpleJdbcCall utilizando el JdbcTemplate inyectado. SimpleJdbcCall 
			 *  se utiliza para invocar procedimientos almacenados de forma más sencilla en comparación con el uso directo de JdbcTemplate.
			 */
			
			jdbcTemplate.setResultsMapCaseInsensitive(true);
		
			//System.out.println("db_catalogo="+db_catalogo);
			
			SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("obtener_nombre_cliente")
					.withCatalogName(db_catalogo)
					.useInParameterNames("p_id_cliente")
					.declareParameters(
							new SqlParameter("p_id_cliente", Types.NUMERIC), 
							new SqlOutParameter("p_nombre", Types.VARCHAR)
							);

			
			// La razón principal por la que se utiliza Map<String, Object> en lugar de Map<String, String>
			Map<String, Object> inParams = new HashMap<>();
			inParams.put("p_id_cliente", idCliente);
			
			Map<String, Object> outParams = jdbcCall.execute(inParams);
			
			/*
			TAMBIEN SE PUEDE HACER ASI MAS CORTO Y FUNCIONA
			
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate);
			SqlParameterSource in = new MapSqlParameterSource().addValue("p_id_cliente", idCliente);
			Map<String, Object> out = simpleJdbcCall.withProcedureName("obtener_nombre_cliente").withCatalogName("db_banco").execute(in);
			
			 */
			
			if (outParams.get("p_nombre") != null && outParams.containsKey("p_nombre")) {
				return (String) outParams.get("p_nombre");
			}
			else	{
				return null;
				//throw new SQLException("El procedimiento no devolvió el nombre del usuario.");
			}
			
		} 
		// DataAccessException, es la excepción base para las excepciones relacionadas con el acceso a datos en Spring.
		catch (DataAccessException ex) {
			throw new SQLException("Error al ejecutar el procedimiento almacenado.", ex);
		}
		

		
	} // obtenerNombreCompletoCliente
	
	
}
