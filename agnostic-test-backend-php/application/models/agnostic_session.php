<?php
class Agnostic_session extends CI_Model {
	
	/**
	 * Gets a session parameter that comes from proxy server
	 * @param string $key Key of the desired session parameter
	 * @return string Session param value
	 */
	function get_attribute($key) {
		$headers = $this->input->request_headers();
		$session_header = $headers["X-session"];
		return $this->get_session_array($session_header)[$key];
	}
	
	/**
	 * Formats session header string into associative array
	 * @param string $session_header Session header in format "key1=value1&key2=value2&..."
	 * @return Associative array in form "key => value"
	 */
	function get_session_array($session_header) {
		$a = explode('&', $session_header);
	
		foreach ($a as $result) {
		    $b = explode('=', $result);
		    $array[$b[0]] = $b[1];
		}
		
		return $array;
	}
	
}
