<?php
class Modules extends CI_Model {
	
	/**
	 * Selects all internal modules from database
	 * @return array Array of internal modules
	 */
	function get_modules() {
		$this->db->select('*');
		$this->db->from("cms_modules");
		$this->db->where("activated", "true");
		$this->db->order_by("order_num", "id");
		$query = $this->db->get();
		return $query->result();
	}
	
	/**
	 * Selects all external modules from database
	 * @return array Array of external modules
	 */
	function get_external_modules() {
		$this->db->select('*');
		$this->db->from("cms_external_modules");
		$this->db->where("activated", "true");
		$this->db->order_by("order_num", "id");
		$query = $this->db->get();
		return $query->result();
	}
	
}