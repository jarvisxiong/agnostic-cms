<?php
$max_birth_date = date('Y') - 15;

$config = array(	
	"products/multiplier" => array(
		array (
			'field'   => 'multiplier',
			'label'   => 'multiplier',
			'rules'   => 'required|preprocess_decimal|decimal'
		)
	)
);