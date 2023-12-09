select count(*) from pg_aggregate;

insert into util (id, name, value) values
   (1, 'wifi_ssid', 'UPC8894AD1'),
   (2, 'wifi_pass', '[REDACTED]')
;

insert into data_type (id, name) values
    (1, 'BOOLEAN'),
    (2, 'INTEGER'),
    (3, 'JSON'),
    (4, 'RGB')
;

insert into blueprint (id, name, display_name, no_input_pins, no_output_pins, no_params, is_hardware, is_valid) values
    (1, 'vlx_led', 'LED agent', 2, 0, 0, true, true),
    (2, 'vlx_rgb', 'RGB agent', 2, 0, 0, true, true),
    (3, 'vlx_switch', 'Switch agent', 0, 1, 2, true, true),
    (4, 'vlx_slider', 'Slider agent', 0, 1, 2, true, true)
;

insert into param (id, name, data_type_id, blueprint_id, default_value) values
    (1, 'Publish on change', 1, 3, 'true'),
    (2, 'Push period (ms)', 2, 3, '500'),
    (3, 'Publish on change', 1, 4, 'true'),
    (4, 'Push period (ms)', 2, 4, '500')
;

insert into pin (id, name, type, data_type_id, default_value, blueprint_id) values
    (1, 'Power', 0, 1, 'false', 1),
    (2, 'Brightness', 0, 2, '100', 1),
    (3, 'Power', 0, 1, 'false', 2),
    (4, 'Color', 0, 4, '[0, 0, 0]', 2),
    (5, 'Value', 1, 1, 'false', 3),
    (6, 'Value', 1, 2, '0', 4)
;

-- insert into agent (id, name, blueprint_id) values
--     (1, 'test', 1),
--     (2, 'test_hw', 3)
-- ;
--
-- insert into hw_agent (id, mac_addr, agent_id) values
--     (1, 'macmac', 2)
-- ;
--
-- insert into agent_pin (id, pin_id, agent_id, last_value, src_pin_id) values
--     (1, 1, 1, 'true', null),
--     (2, 5, 2, 'true', null)
-- ;
--
-- insert into agent_param (id, param_id, agent_id, value) values
--     (1, 1, 2, 'false')
-- ;