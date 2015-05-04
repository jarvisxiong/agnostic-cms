<?php

$config = array(
	
	// validation rules for price multiplier form
	"products/multiplier" => array(
		array (
			'field'   => 'multiplier',
			'label'   => 'multiplier',
			'rules'   => 'required|preprocess_decimal|decimal'
		)
	)
);