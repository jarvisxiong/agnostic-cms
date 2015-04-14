<?php
class Modules extends CI_Model {
	
	function get_modules() {
		$this->db->select('*');
		$this->db->from("cms_modules");
		$this->db->where("activated", "true");
		$this->db->order_by("order_num");
		$query = $this->db->get();
		return $query->result();
	}
	
	function get_external_modules() {
		$this->db->select('*');
		$this->db->from("cms_external_modules");
		$this->db->where("activated", "true");
		$this->db->order_by("order_num");
		$query = $this->db->get();
		return $query->result();
	}
	
}