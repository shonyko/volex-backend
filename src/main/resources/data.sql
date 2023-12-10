select count(*) from pg_aggregate;

insert into util (name, value) values
   ('wifi_ssid', 'UPC8894AD1'),
   ('wifi_pass', '[REDACTED]')
;

insert into data_type (name) values
    ('BOOLEAN'),
    ('INTEGER'),
    ('JSON'),
    ('RGB')
;

insert into blueprint (name, display_name, no_input_pins, no_output_pins, no_params, is_hardware, is_valid) values
    ('vlx_led', 'LED agent', 2, 0, 0, true, true),
    ('vlx_rgb', 'RGB agent', 2, 0, 0, true, true),
    ('vlx_switch', 'Switch agent', 0, 1, 2, true, true),
    ('vlx_slider', 'Slider agent', 0, 1, 2, true, true)
;

insert into param (name, data_type_id, blueprint_id, default_value) values
    ('Publish on change', 1, 3, 'true'),
    ('Push period (ms)', 2, 3, '5000'),
    ('Publish on change', 1, 4, 'true'),
    ('Push period (ms)', 2, 4, '5000')
;

insert into pin (name, type, data_type_id, default_value, blueprint_id) values
    ('Power', 0, 1, 'false', 1),
    ('Brightness', 0, 2, '100', 1),
    ('Power', 0, 1, 'false', 2),
    ('Color', 0, 4, '[0, 0, 0]', 2),
    ('Value', 1, 1, 'false', 3),
    ('Value', 1, 2, '0', 4)
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