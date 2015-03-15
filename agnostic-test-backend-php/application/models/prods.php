<?php
class Prods extends CI_Model {
	
	function apply_price_multiplier($multiplier) {
		$this->db->query("UPDATE products SET price = price * ?", array($multiplier));
	}
	
	function get_all_products() {
		$this->db->select('*');
		$this->db->from("products");
		$this->db->order_by("order_num");
		$query = $this->db->get();
		return $query->result();
	}
	
}