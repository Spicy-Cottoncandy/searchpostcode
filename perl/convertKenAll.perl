#!/usr/bin/perl

use strict;

my $INPUT = '/home/ec2-user/KEN_ALL_utf8.csv';
my $OUTPUT = '/home/ec2-user/import.csv';

open my $infh, '<', $INPUT;
open my $outfh, '>', $OUTPUT;

my $i = 0;
for my $line (<$infh>){
    chomp $line;
    my @fields = split /,/, $line;
    my @outfields = ($i, $fields[2], $fields[3], $fields[4], $fields[5], $fields[6], $fields[7], $fields[8]);
    print $outfh join(',', @outfields) . "\n";
    $i++;
}
close $infh;
close $outfh;

