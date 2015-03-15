<?php
class Agnostic_session extends CI_Model {
	
	function get_attribute($key) {
		$headers = $this->input->request_headers();
		$session_header = $headers["X-session"];
		return $this->get_session_array($session_header)[$key];
	}
	
	function get_session_array($session_header) {
		$a = explode('&', $session_header);
	
		foreach ($a as $result) {
		    $b = explode('=', $result);
		    $array[$b[0]] = $b[1];
		}
		
		return $array;
	}
	
}
