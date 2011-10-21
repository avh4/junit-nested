#!/usr/bin/env ruby
require 'liquid'
require 'yaml'
require 'fileutils'

# Open the properties file
properties_file = File.open "properties.yaml"
properties_yaml = properties_file.read
puts properties_yaml
puts
# Ask the user if the properties are correct
puts "Are these properties correct?"
reply = gets
if reply[0] != "y" then
  puts "Aborting.  Edit properties.yaml to make changes."
  exit
end

# Load the properties
properties = YAML::load(properties_yaml)

# process the files
files = [ "pom.xml", ".project" ]
files.each do |file|
  puts "Processing #{file}..."
  # read the file
  template = File.open(file, "r").read
  # process the file
  rendering = Liquid::Template.parse(template).render properties
  # write the file
  File.open(file, "w").write(rendering)
end

# Create empty directories
package_path = "#{properties['package']}.#{properties['project']}".tr('.', '/').tr('-', '')
dirs = [
  "src/main/java/#{package_path}", "src/main/resources",
  "src/test/java/#{package_path}", "src/test/resources",
  "src/features/java/#{package_path}", "src/features/stories"
  ]
dirs.each do |dir|
  puts "Creating #{dir}..."
  FileUtils.mkdir_p dir
end

puts "Successful.  Removing init.rb..."
File.delete "init.rb"

puts "Done!"