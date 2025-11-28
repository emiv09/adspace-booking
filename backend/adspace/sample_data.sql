INSERT INTO ad_space (name, type, city, address, price_per_day, status, created_at, updated_at)
VALUES
  ('Times Square Billboard', 'BILLBOARD', 'New York', '1 Times Square', 500.00, 'AVAILABLE', NOW(), NOW()),
  ('Broadway Digital Display', 'BILLBOARD', 'New York', 'Broadway & 42nd St', 750.00, 'AVAILABLE', NOW(), NOW()),
  ('Central Park Bus Stop', 'BUS_STOP', 'New York', 'Central Park West & 72nd St', 150.00, 'AVAILABLE', NOW(), NOW()),
  ('Union Square Bus Shelter', 'BUS_STOP', 'New York', 'Union Square', 120.00, 'AVAILABLE', NOW(), NOW()),
  ('Mall of America Display', 'MALL_DISPLAY', 'Minneapolis', '60 E Broadway, Bloomington', 300.00, 'AVAILABLE', NOW(), NOW()),
  ('Westfield Century City', 'MALL_DISPLAY', 'Los Angeles', '10250 Santa Monica Blvd', 400.00, 'AVAILABLE', NOW(), NOW()),
  ('NYC Subway Car Ad', 'TRANSIT_AD', 'New York', 'MTA Subway Network', 200.00, 'AVAILABLE', NOW(), NOW()),
  ('Metro Bus Wrap', 'TRANSIT_AD', 'Los Angeles', 'LA Metro Bus Network', 350.00, 'AVAILABLE', NOW(), NOW()),
  ('Downtown LA Billboard', 'BILLBOARD', 'Los Angeles', 'Figueroa St', 600.00, 'AVAILABLE', NOW(), NOW()),
  ('Santa Monica Pier Display', 'BILLBOARD', 'Los Angeles', 'Santa Monica Pier', 550.00, 'MAINTENANCE', NOW(), NOW());

SELECT * FROM ad_space;

